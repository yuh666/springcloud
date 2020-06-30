package org.yuhao.springcloud.order.ribbon;

import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.client.http.RestClient;

import java.util.ArrayList;
import java.util.List;

public class RibbonMain {

    public static void main(String[] args) throws Exception {
//        ConfigurationManager.getConfigInstance().setProperty("payment-service.ribbon.listOfServers",
//                "localhost:8001,localhost:8002");
//        RestClient restClient = (RestClient)ClientFactory.getNamedClient("payment-service");
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri("/payment/lb").build();
//        for(int i = 0; i < 10; i++) {
//            HttpResponse response = restClient.executeWithLoadBalancer(request);
//            String result = response.getEntity(String.class);
//            System.err.println(result);
//        }

        ILoadBalancer balancer = new BaseLoadBalancer();
        List<Server> servers = new ArrayList<>();
        servers.add(new Server("localhost", 8080));
        servers.add(new Server("localhost", 8088));
        balancer.addServers(servers);

        for(int i = 0; i < 10; i++) {
            Server server = balancer.chooseServer(null);
            System.out.println(server);
        }

    }
}
