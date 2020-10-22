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
package com.futurewei.alcor.dataplane.client.grpc;

import com.futurewei.alcor.dataplane.client.DataPlaneClient;
import com.futurewei.alcor.dataplane.config.grpc.GoalStateProvisionerClient;
import com.futurewei.alcor.schema.Goalstate;
import com.futurewei.alcor.web.entity.dataplane.MulticastGoalState;
import com.futurewei.alcor.web.entity.dataplane.UnicastGoalState;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "mq", name = "type", havingValue = "grpc")
public class DataPlaneClientImpl implements DataPlaneClient {
    private int grpcPort = 6677;

    @Override
    public void createGoalStates(Goalstate.GoalState goalState, String hostIp) throws Exception {

    }

    @Override
    public void createGoalStates(List<UnicastGoalState> unicastGoalStates) throws Exception {
        for (UnicastGoalState unicastGoalState: unicastGoalStates) {
            GoalStateProvisionerClient goalStateProvisionerClient =
                    new GoalStateProvisionerClient(unicastGoalState.getNextTopic(), grpcPort);
            goalStateProvisionerClient.PushNetworkResourceStates(unicastGoalState.getGoalState());
            goalStateProvisionerClient.shutdown();
        }
    }

    @Override
    public void updateGoalStates(List<UnicastGoalState> unicastGoalStates) throws Exception {

    }

    @Override
    public void deleteGoalStates(List<UnicastGoalState> unicastGoalStates) throws Exception {

    }

    @Override
    public void createGoalState(MulticastGoalState multicastGoalState) throws Exception {

    }

    @Override
    public void updateGoalState(MulticastGoalState multicastGoalState) throws Exception {

    }

    @Override
    public void deleteGoalState(MulticastGoalState multicastGoalState) throws Exception {

    }

    @Override
    public void createGoalStates(List<UnicastGoalState> unicastGoalStates, MulticastGoalState multicastGoalState) throws Exception {

    }
}
