package uk.me.mattgill.samples.jaxrs.cluster.ping.event.entity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

@ApplicationScoped
public class HazelcastResources {

    @Inject
    private HazelcastInstance instance;

    /**
     * Gets the UUID of a random instance in the cluster. Will only return
     * the current instance if the cluster only contains itself.
     */
    public String getRandomInstanceId() {
        // Get all cluster members
        Set<Member> members = new HashSet<>(instance.getCluster().getMembers());

        // Remove the local instance, unless it's the only instance
        if (members.size() > 1) {
            members.removeIf(x -> x.getUuid().equals(getLocalInstanceId()));
        }

        // Pick a random instance
        int randomInstanceNumber = new Random().nextInt(members.size());
        Member randomMember = members.toArray(new Member[0])[randomInstanceNumber];

        // Return the random instance's UUID.
        return randomMember.getUuid();
    }

    /**
     * Gets the UUID of the local instance.
     */
    public String getLocalInstanceId() {
        return instance.getCluster().getLocalMember().getUuid();
    }

}