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
package com.futurewei.alcor.dataplane.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futurewei.alcor.dataplane.client.DataPlaneClient;
import com.futurewei.alcor.dataplane.config.UnitTestConfig;
import com.futurewei.alcor.schema.Common.OperationType;
import com.futurewei.alcor.schema.Common.ResourceType;
import com.futurewei.alcor.web.entity.dataplane.*;
import com.futurewei.alcor.web.entity.dataplane.NeighborEntry.NeighborType;
import com.futurewei.alcor.web.entity.port.PortEntity;
import com.futurewei.alcor.web.entity.securitygroup.SecurityGroup;
import com.futurewei.alcor.web.entity.vpc.VpcEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DataPlaneTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataPlaneClient dataPlaneClient;

    private List<VpcEntity> buildVpcEntities() {
        VpcEntity vpcEntity = new VpcEntity();
        vpcEntity.setProjectId(UnitTestConfig.projectId);
        vpcEntity.setId(UnitTestConfig.vpcId);
        vpcEntity.setName(UnitTestConfig.vpcName);
        vpcEntity.setCidr(UnitTestConfig.vpcCidr);
        vpcEntity.setTenantId(UnitTestConfig.tenantId);

        List<VpcEntity> vpcEntities = new ArrayList<>();
        vpcEntities.add(vpcEntity);

        return vpcEntities;
    }

    private List<InternalSubnetEntity> buildSubnetEntities() {
        InternalSubnetEntity internalSubnetEntity1 = new InternalSubnetEntity();
        internalSubnetEntity1.setId(UnitTestConfig.subnetId1);
        internalSubnetEntity1.setProjectId(UnitTestConfig.projectId);
        internalSubnetEntity1.setVpcId(UnitTestConfig.vpcId);
        internalSubnetEntity1.setName(UnitTestConfig.subnetName1);
        internalSubnetEntity1.setCidr(UnitTestConfig.subnetCidr1);
        internalSubnetEntity1.setTunnelId(UnitTestConfig.tunnelId);
        internalSubnetEntity1.setGatewayIp(UnitTestConfig.gatewayIp1);
        internalSubnetEntity1.setGatewayMacAddress(UnitTestConfig.gatewayMacAddress1);
        internalSubnetEntity1.setDhcpEnable(true);
        internalSubnetEntity1.setAvailabilityZone(UnitTestConfig.availabilityZone);
        internalSubnetEntity1.setPrimaryDns(UnitTestConfig.primaryDns);
        internalSubnetEntity1.setSecondaryDns(UnitTestConfig.secondaryDns);

        InternalSubnetEntity internalSubnetEntity2 = new InternalSubnetEntity();
        internalSubnetEntity2.setId(UnitTestConfig.subnetId2);
        internalSubnetEntity2.setProjectId(UnitTestConfig.projectId);
        internalSubnetEntity2.setVpcId(UnitTestConfig.vpcId);
        internalSubnetEntity2.setName(UnitTestConfig.subnetName2);
        internalSubnetEntity2.setCidr(UnitTestConfig.subnetCidr2);
        internalSubnetEntity2.setTunnelId(UnitTestConfig.tunnelId);
        internalSubnetEntity2.setGatewayIp(UnitTestConfig.gatewayIp2);
        internalSubnetEntity2.setGatewayMacAddress(UnitTestConfig.gatewayMacAddress2);
        internalSubnetEntity2.setDhcpEnable(true);
        internalSubnetEntity2.setAvailabilityZone(UnitTestConfig.availabilityZone);
        internalSubnetEntity2.setPrimaryDns(UnitTestConfig.primaryDns);
        internalSubnetEntity2.setSecondaryDns(UnitTestConfig.secondaryDns);

        List<InternalSubnetEntity> subnetEntities = new ArrayList<>();
        subnetEntities.add(internalSubnetEntity1);
        subnetEntities.add(internalSubnetEntity2);

        return subnetEntities;
    }

    private List<InternalPortEntity> buildPortEntities() {
        InternalPortEntity internalPortEntity = new InternalPortEntity();
        internalPortEntity.setId(UnitTestConfig.portId1);
        internalPortEntity.setProjectId(UnitTestConfig.projectId);
        internalPortEntity.setVpcId(UnitTestConfig.vpcId);
        internalPortEntity.setName(UnitTestConfig.portName1);
        internalPortEntity.setMacAddress(UnitTestConfig.mac1);
        internalPortEntity.setAdminStateUp(true);
        List<PortEntity.FixedIp> fixedIps = new ArrayList<>();
        fixedIps.add(new PortEntity.FixedIp(UnitTestConfig.subnetId1, UnitTestConfig.ip11));
        internalPortEntity.setFixedIps(fixedIps);
        //internalPortEntity.setAllowedAddressPairs();
        //List<String> securityGroupIds = new ArrayList<>();
        //securityGroupIds.add(UnitTestConfig.securityGroupId1);
        //securityGroupIds.add(UnitTestConfig.securityGroupId2);
        //internalPortEntity.setSecurityGroups(securityGroupIds);
        internalPortEntity.setBindingHostIP(UnitTestConfig.hostIp1);

        List<InternalPortEntity> portEntities = new ArrayList<>();
        portEntities.add(internalPortEntity);

        return portEntities;
    }

    private Map<String, NeighborInfo> buildNeighborInfos() {
        Map<String, NeighborInfo> neighborInfos = new HashMap<>();
        NeighborInfo neighborInfo11 = new NeighborInfo();
        neighborInfo11.setHostIp(UnitTestConfig.hostIp1);
        neighborInfo11.setPortIp(UnitTestConfig.ip11);
        neighborInfo11.setVpcId(UnitTestConfig.vpcId);
        neighborInfo11.setSubnetId(UnitTestConfig.subnetId1);
        neighborInfo11.setPortId(UnitTestConfig.portId1);
        neighborInfo11.setPortMac(UnitTestConfig.mac1);

        NeighborInfo neighborInfo12 = new NeighborInfo();
        neighborInfo12.setHostIp(UnitTestConfig.hostIp1);
        neighborInfo12.setPortIp(UnitTestConfig.ip12);
        neighborInfo12.setVpcId(UnitTestConfig.vpcId);
        neighborInfo12.setSubnetId(UnitTestConfig.subnetId1);
        neighborInfo12.setPortId(UnitTestConfig.portId2);
        neighborInfo12.setPortMac(UnitTestConfig.mac1);

        NeighborInfo neighborInfo21 = new NeighborInfo();
        neighborInfo21.setHostIp(UnitTestConfig.hostIp2);
        neighborInfo21.setPortIp(UnitTestConfig.ip21);
        neighborInfo21.setVpcId(UnitTestConfig.vpcId);
        neighborInfo21.setSubnetId(UnitTestConfig.subnetId2);
        neighborInfo21.setPortId(UnitTestConfig.portId3);
        neighborInfo21.setPortMac(UnitTestConfig.mac1);

        NeighborInfo neighborInfo22 = new NeighborInfo();
        neighborInfo22.setHostIp(UnitTestConfig.hostIp2);
        neighborInfo22.setPortIp(UnitTestConfig.ip22);
        neighborInfo22.setVpcId(UnitTestConfig.vpcId);
        neighborInfo22.setSubnetId(UnitTestConfig.subnetId2);
        neighborInfo22.setPortId(UnitTestConfig.portId4);
        neighborInfo22.setPortMac(UnitTestConfig.mac1);

        neighborInfos.put(UnitTestConfig.ip11, neighborInfo11);
        neighborInfos.put(UnitTestConfig.ip12, neighborInfo12);
        neighborInfos.put(UnitTestConfig.ip21, neighborInfo21);
        neighborInfos.put(UnitTestConfig.ip22, neighborInfo22);

        return neighborInfos;
    }

    private Map<String, List<NeighborEntry>> buildNeighborTable() {
        Map<String, List<NeighborEntry>> neighborTable = new HashMap<>();
        List<NeighborEntry> neighborEntries = new ArrayList<>();

        NeighborEntry neighborEntry1 = new NeighborEntry();
        neighborEntry1.setLocalIp(UnitTestConfig.ip11);
        neighborEntry1.setNeighborIp(UnitTestConfig.ip12);
        neighborEntry1.setNeighborType(NeighborType.L2);

        NeighborEntry neighborEntry2 = new NeighborEntry();
        neighborEntry2.setLocalIp(UnitTestConfig.ip11);
        neighborEntry2.setNeighborIp(UnitTestConfig.ip21);
        neighborEntry2.setNeighborType(NeighborType.L3);

        NeighborEntry neighborEntry3 = new NeighborEntry();
        neighborEntry3.setLocalIp(UnitTestConfig.ip11);
        neighborEntry3.setNeighborIp(UnitTestConfig.ip22);
        neighborEntry3.setNeighborType(NeighborType.L3);

        neighborEntries.add(neighborEntry1);
        neighborEntries.add(neighborEntry2);
        neighborEntries.add(neighborEntry3);
        neighborTable.put(UnitTestConfig.ip11, neighborEntries);

        return neighborTable;
    }

    private List<SecurityGroup> buildSecurityGroups() {
        List<SecurityGroup> securityGroups = new ArrayList<>();
        return securityGroups;
    }

    private NetworkConfiguration buildNetworkConfiguration() {
        NetworkConfiguration networkConfiguration = new NetworkConfiguration();
        networkConfiguration.setRsType(ResourceType.PORT);
        networkConfiguration.setOpType(OperationType.CREATE);
        networkConfiguration.setVpcs(buildVpcEntities());
        networkConfiguration.setSubnets(buildSubnetEntities());
        networkConfiguration.setPortEntities(buildPortEntities());
        networkConfiguration.setNeighborInfos(buildNeighborInfos());
        networkConfiguration.setNeighborTable(buildNeighborTable());
        networkConfiguration.setSecurityGroups(buildSecurityGroups());

        return networkConfiguration;
    }
    @Test
    public void createNetworkConfigurationTest() throws Exception {
        NetworkConfiguration networkConfiguration = buildNetworkConfiguration();
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(networkConfiguration);

        this.mockMvc.perform(post(UnitTestConfig.url)
                .content(body)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
