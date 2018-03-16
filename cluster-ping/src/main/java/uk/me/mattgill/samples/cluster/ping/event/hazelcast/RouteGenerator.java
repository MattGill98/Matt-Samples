package uk.me.mattgill.samples.cluster.ping.event.hazelcast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

@ApplicationScoped
public class RouteGenerator {

    @Inject
    private HazelcastInstance instance;

    private static final int maxRouteLength = 6;

    public Collection<String> getRoute() {
        // Get a random list of all instances
        List<String> route = instance.getCluster().getMembers().stream()
                .map(member -> member.getAttributes().get("GLASSFISH-INSTANCE").toString())
                .collect(Collectors.toList());
        Collections.shuffle(route);

        // Remove the local instance
        String localInstanceName = instance.getCluster().getLocalMember().getAttributes().get("GLASSFISH-INSTANCE")
                .toString();
        route.remove(localInstanceName);

        // Cut down to size
        while (route.size() > maxRouteLength) {
            route.remove(route.size() - 1);
        }

        // Always send to the local instance at the end
        route.add(localInstanceName);

        return route;
    }

}