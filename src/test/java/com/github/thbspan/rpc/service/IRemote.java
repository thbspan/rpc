package com.github.thbspan.rpc.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemote extends Remote {
    String echo() throws RemoteException;
}
