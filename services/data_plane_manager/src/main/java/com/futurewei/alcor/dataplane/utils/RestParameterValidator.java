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
package com.futurewei.alcor.dataplane.utils;

import com.futurewei.alcor.web.entity.dataplane.InternalPortEntity;
import com.futurewei.alcor.web.entity.dataplane.InternalSubnetEntity;
import com.futurewei.alcor.web.entity.dataplane.NetworkConfiguration;
import com.futurewei.alcor.web.entity.securitygroup.SecurityGroup;
import com.futurewei.alcor.web.entity.vpc.VpcEntity;

import java.util.List;

public class RestParameterValidator {

    public static void checkVpcEntities(List<VpcEntity> vpcEntities) throws Exception {

    }

    public static void checkSubnetEntities(List<InternalSubnetEntity> subnetEntities) throws Exception {

    }

    public static void checkPortEntities(List<InternalPortEntity> portEntities) throws Exception {

    }

    public static void checkNeighborTable(NetworkConfiguration networkConfig) throws Exception {

    }

    public static void checkSecurityGroups(List<SecurityGroup> securityGroups) throws Exception {

    }

    public static void checkNetworkConfiguration(NetworkConfiguration networkConfig) throws Exception {
        checkVpcEntities(networkConfig.getVpcs());
        checkSubnetEntities(networkConfig.getSubnets());
        checkPortEntities(networkConfig.getPortEntities());
        checkNeighborTable(networkConfig);
        checkSecurityGroups(networkConfig.getSecurityGroups());
    }
}
