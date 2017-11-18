package com.pwned.line.service;

import com.pwned.line.job.PushKMB;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@kmb::notify]
 * Resolved params: []
 * @author Timothy Pak
 */

public class KMBNotify extends DefaultService{

    public KMBNotify(Service service){
        super(service);
    }

    @Override
    public void payload() throws Exception{
        PushKMB.updateKMB();
    }

    @Override
    public Service chain() throws Exception {
        return this;
    }
}
