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

package com.futurewei.alcor.apigateway.proxies;

import com.futurewei.alcor.apigateway.vpc.VpcWebDestinations;
import com.futurewei.alcor.common.entity.ResponseId;
import com.futurewei.alcor.web.entity.VpcWebJson;
import com.futurewei.alcor.web.exception.VpcNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class VpcManagerServiceProxy {

    private VpcWebDestinations vpcWebDestinations;

    private WebClient webClient;

    public VpcManagerServiceProxy(VpcWebDestinations destinations, WebClient client) {
        this.vpcWebDestinations = destinations;
        this.webClient = client;
    }

    public Mono<VpcWebJson> findVpcById(String projectId, String vpcId) {
        Mono<ClientResponse> response = webClient
                .get()
                .uri(vpcWebDestinations.getVpcManagerServiceUrl() + "/project/{projectId}/vpcs/{vpcId}", projectId, vpcId)
                .exchange();
        return response.flatMap(resp -> {
            switch (resp.statusCode()) {
                case OK:
                    return resp.bodyToMono(VpcWebJson.class);
                case NOT_FOUND:
                    return Mono.error(new VpcNotFoundException());
                default:
                    return Mono.error(new RuntimeException("Unknown" + resp.statusCode()));
            }
        });
    }

    public Mono<VpcWebJson> createVpc(UUID projectId, Mono<VpcWebJson> newVpcJson) {
        Mono<ClientResponse> response = webClient
                .post()
                .uri(vpcWebDestinations.getVpcManagerServiceUrl() + "/project/{projectId}/vpcs", projectId)
                .body(newVpcJson, VpcWebJson.class)
                .exchange();
        return response.flatMap(resp -> {
            switch (resp.statusCode()) {
                case CREATED:
                    return resp.bodyToMono(VpcWebJson.class);
                case NOT_FOUND:
                    return Mono.error(new VpcNotFoundException());
                default:
                    return Mono.error(new RuntimeException("Unknown" + resp.statusCode()));
            }
        });
    }

    public Mono<ResponseId> deleteVpcById(String projectId, String vpcId) {
        Mono<ClientResponse> response = webClient
                .delete()
                .uri(vpcWebDestinations.getVpcManagerServiceUrl() + "/project/{projectId}/vpcs/{vpcId}", projectId, vpcId)
                .exchange();
        return response.flatMap(resp -> {
            switch (resp.statusCode()) {
                case OK:
                    return resp.bodyToMono(ResponseId.class);
                case NOT_FOUND:
                    return Mono.error(new VpcNotFoundException());
                default:
                    return Mono.error(new RuntimeException("Unknown" + resp.statusCode()));
            }
        });
    }

    public Mono<String> getHealthStatus() {
        Mono<ClientResponse> healthStatusResponse = webClient
                .get()
                .uri(vpcWebDestinations.getVpcManagerServiceUrl() + "/actuator/health")
                .exchange();
        return healthStatusResponse.flatMap(resp -> {
            switch (resp.statusCode()) {
                case OK:
                    return resp.bodyToMono(String.class);
                default:
                    return Mono.error(new RuntimeException("Unknown" + resp.statusCode()));
            }
        });
    }
}
