/*
Copyright 2019 The Alcor Authors.

Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/
package com.futurewei.alcor.portmanager.request;

import com.futurewei.alcor.common.utils.SpringContextUtil;
import com.futurewei.alcor.portmanager.exception.GetSubnetEntityException;
import com.futurewei.alcor.portmanager.processor.PortContext;
import com.futurewei.alcor.web.entity.subnet.SubnetEntity;
import com.futurewei.alcor.web.entity.subnet.SubnetWebJson;
import com.futurewei.alcor.web.restclient.SubnetManagerRestClient;

import java.util.ArrayList;
import java.util.List;

public class FetchSubnetRequest extends AbstractRequest {
    private SubnetManagerRestClient subnetManagerRestClient;
    private List<String> subnetIds;
    private List<SubnetEntity> subnetEntities;
    private boolean afterRandomIp;

    public FetchSubnetRequest(PortContext context, List<String> subnetIds, boolean afterRandomIp) {
        super(context);
        this.subnetIds = subnetIds;
        this.afterRandomIp = afterRandomIp;
        this.subnetEntities = new ArrayList<>();
        subnetManagerRestClient = SpringContextUtil.getBean(SubnetManagerRestClient.class);
    }

    public List<SubnetEntity> getSubnetEntities() {
        return subnetEntities;
    }

    public boolean isAfterRandomIp() {
        return afterRandomIp;
    }

    @Override
    public void send() throws Exception {
        //TODO: Instead by getSubnetsBySubnetIds interface
        for (String subnetId: subnetIds) {
            SubnetWebJson subnetWebJson = subnetManagerRestClient.getSubnet(context.getProjectId(), subnetId);
            if (subnetWebJson == null || subnetWebJson.getSubnet() == null) {
                throw new GetSubnetEntityException();
            }

            subnetEntities.add(subnetWebJson.getSubnet());
        }
    }

    @Override
    public void rollback() {

    }
}
