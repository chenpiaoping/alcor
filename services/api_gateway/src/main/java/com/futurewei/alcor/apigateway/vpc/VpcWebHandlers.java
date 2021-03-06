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

package com.futurewei.alcor.apigateway.vpc;

import com.futurewei.alcor.apigateway.proxies.VpcManagerServiceProxy;
import com.futurewei.alcor.common.entity.ResponseId;
import com.futurewei.alcor.common.utils.CommonUtil;
import com.futurewei.alcor.web.entity.VpcWebJson;
import com.futurewei.alcor.web.exception.VpcNotFoundException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public class VpcWebHandlers {

    private VpcManagerServiceProxy vpcManager;

    //private RouteManagerServiceProxy routeManager;

    public VpcWebHandlers(VpcManagerServiceProxy vpcManager) {
        this.vpcManager = vpcManager;
        //  this.routeManage = routeManager;
    }

    public Mono<ServerResponse> getVpc(ServerRequest request) {
        String projectId = request.pathVariable("projectId");
        String vpcId = request.pathVariable("vpcId");

        Mono<VpcWebJson> vpcInfo = vpcManager.findVpcById(projectId, vpcId);

        // Add Route Manager here

        return vpcInfo.flatMap(od -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(fromObject(od)))
                .onErrorResume(VpcNotFoundException.class, e -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createVpc(ServerRequest request) {

        final Mono<VpcWebJson> vpcObj = request.bodyToMono(VpcWebJson.class);
        final UUID projectId = UUID.fromString(request.pathVariable("projectId"));

        final UUID generatedVpcId = UUID.randomUUID();
        Mono<VpcWebJson> newVpcObj = vpcObj.map(p ->
                p.getVpc().getId().isEmpty() || !CommonUtil.isUUID(p.getVpc().getId()) ?
                        new VpcWebJson(p.getVpc(), generatedVpcId) : new VpcWebJson(p.getVpc()));

        Mono<VpcWebJson> response = vpcManager.createVpc(projectId, newVpcObj);

        return response.flatMap(od -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(fromObject(od)))
                .onErrorResume(VpcNotFoundException.class, e -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteVpc(ServerRequest request) {

        String projectId = request.pathVariable("projectId");
        String vpcId = request.pathVariable("vpcId");

        Mono<ResponseId> responseWithId = vpcManager.deleteVpcById(projectId, vpcId);

        // Add Route Manager here

        return responseWithId.flatMap(od -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(fromObject(od)))
                .onErrorResume(VpcNotFoundException.class, e -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getVpcManagerHealthStatus(ServerRequest request) {

        Mono<String> healthStatus = vpcManager.getHealthStatus();

        return healthStatus.flatMap(od -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(fromObject(od)))
                .onErrorResume(e -> ServerResponse.notFound().build());
    }
}
