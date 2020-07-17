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
import com.futurewei.alcor.portmanager.exception.GetSecurityGroupException;
import com.futurewei.alcor.portmanager.processor.PortContext;
import com.futurewei.alcor.web.entity.securitygroup.SecurityGroup;
import com.futurewei.alcor.web.entity.securitygroup.SecurityGroupJson;
import com.futurewei.alcor.web.restclient.SecurityGroupManagerRestClient;

import java.util.ArrayList;
import java.util.List;

public class FetchSecurityGroupRequest extends AbstractRequest {
    private SecurityGroupManagerRestClient securityGroupManagerRestClient;
    private List<String> securityGroupIds;
    private List<String> defaultSecurityGroupIds;
    private List<SecurityGroup> securityGroups;

    public FetchSecurityGroupRequest(PortContext context, List<String> securityGroupIds, List<String> defaultSecurityGroupIds) {
        super(context);
        this.securityGroupIds = securityGroupIds;
        this.defaultSecurityGroupIds = defaultSecurityGroupIds;
        this.securityGroups = new ArrayList<>();
        this.securityGroupManagerRestClient = SpringContextUtil.getBean(SecurityGroupManagerRestClient.class);
    }

    public List<SecurityGroup> getSecurityGroups() {
        return securityGroups;
    }

    @Override
    public void send() throws Exception {
        if (securityGroupIds != null) {
            for (String tenantId : securityGroupIds) {
                SecurityGroupJson securityGroup = securityGroupManagerRestClient
                        .getSecurityGroup(context.getProjectId(), tenantId);
                if (securityGroup == null || securityGroup.getSecurityGroup() == null) {
                    throw new GetSecurityGroupException();
                }

                securityGroups.add(securityGroup.getSecurityGroup());
            }
        }

        if (defaultSecurityGroupIds != null) {
            for (String tenantId : defaultSecurityGroupIds) {
                SecurityGroupJson defaultSecurityGroup = securityGroupManagerRestClient
                        .getDefaultSecurityGroup(context.getProjectId(), tenantId);
                if (defaultSecurityGroup == null || defaultSecurityGroup.getSecurityGroup() == null) {
                    throw new GetSecurityGroupException();
                }

                securityGroups.add(defaultSecurityGroup.getSecurityGroup());
            }
        }
    }

    @Override
    public void rollback() {

    }
}
