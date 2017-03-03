
package com.viii.aidlclient;

import com.viii.aidlclient.MessageCenter;

interface IService {
  void registerCallback(MessageCenter cb);
  void unregisterCallback(MessageCenter cb);
}
