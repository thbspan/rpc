package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.invoker.RmiInvoker;
import com.github.thbspan.rpc.provider.Export;
import com.github.thbspan.rpc.provider.RmiExport;

import java.net.MalformedURLException;

import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RmiProtocol extends Protocol {
    private static final Logger LOGGER = LoggerFactory.getLogger(RmiProtocol.class);

    private static final ConcurrentMap<String, Registry> registries = new ConcurrentHashMap<>();

    public RmiProtocol(String ip, int port) {
        super(ip, port);
    }

    @Override
    public void export(Invoker invoker, Object target) {
        Export export = new RmiExport(invoker);
        super.setExport(invoker.getInterfaceClass().getName(), export);

        registries.computeIfAbsent(getIp() + ":" + getPort(), serverKey -> {
            synchronized (LocateRegistry.class) {
                try {
                    // Retrieve existing registry.
                    Registry reg = LocateRegistry.getRegistry(getIp(), getPort(), null);
                    doExport(reg, invoker, target);
                    return reg;
                } catch (RemoteException ex) {
                    LOGGER.debug("RMI registry access threw exception", ex);
                    LOGGER.info("Could not detect RMI registry - creating new one");
                    try {
                        Registry reg = LocateRegistry.createRegistry(getPort());
                        doExport(reg, invoker, target);
                        return reg;
                    } catch (RemoteException e) {
                        LOGGER.error("create registry exception", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void doExport(Registry reg, Invoker invoker, Object target) throws RemoteException {
        testRegistry(reg);
        Remote exportedObject = getObjectToExport(target);
        UnicastRemoteObject.exportObject(exportedObject, getPort());
        try {
            reg.bind(invoker.getInterfaceClass().getName(), exportedObject);
        } catch (AlreadyBoundException ex) {
            throw new IllegalStateException("Already an RMI object bound for name '"
                    + invoker.getInterfaceClass() + "': " + ex.toString());
        }
    }

    private Remote getObjectToExport(Object target) {
        if (target instanceof Remote) {
            return (Remote) target;
        }

        return null;
    }

    private void testRegistry(Registry registry) throws RemoteException {
        registry.list();
    }

    @Override
    public Invoker refer(String serviceName) {
        Invoker invoker = super.getRefers(serviceName);
        if (invoker != null) {
            return invoker;
        }

        String serviceUrl = getServiceUrl(serviceName);
        try {
            invoker = new RmiInvoker(Naming.lookup(serviceUrl), serviceName);
            super.setRefers(serviceName, invoker);
            return invoker;
        } catch (NotBoundException e) {
            throw new IllegalArgumentException("Service URL(" + serviceUrl + ") is invalid", e);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not find RMI service [" + serviceUrl + "] in RMI registry", e);
        } catch (RemoteException e) {
            throw new RuntimeException("Lookup of RMI stub failed", e);
        }
    }

    @Override
    public String getProtocolName() {
        return "rmi";
    }

    @Override
    public String getPathProvider(String serviceName) {
        return "/rmi/" + serviceName + "/providers";
    }
}
