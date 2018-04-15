package com.github.thbspan.rpc.service;

import java.rmi.RemoteException;

public class IRemoteImpl implements IRemote {
    @Override
    public String echo() throws RemoteException {
        return "test";
    }
}
