package uk.me.mattgill.samples.cluster.ping.event.hazelcast;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

@ApplicationScoped
public class HazelcastResources {

    @Inject
    private HazelcastInstance instance;

    /**
     * @return the ID of a random instance in the cluster. Will return
     * the current instance if the cluster only contains itself.
     */
    public String getRandomInstanceId() {
        return getRandomInstanceId(new LinkedList<>());
    }

    /**
     * @return the ID of a random instance in the cluster. Will return
     * the current instance if the cluster only contains itself.
     * @param excludeList a list of instance IDs to not return.
     */
    public String getRandomInstanceId(List<String> excludeList) {
        // If hazelcast is disabled, return the current instance.
        if (instance == null) {
            return getLocalInstanceId();
        }

        // Get all cluster members
        Set<Member> members = new HashSet<>(instance.getCluster().getMembers());

        // Remove everything from the exclude list, and the local instance (unless it's the only instance)
        if (members.size() <= 1) {
            return getLocalInstanceId();
        }
        members.removeIf(x -> x.getUuid().equals(getLocalInstanceId()) || excludeList.contains(x.getUuid()));

        // Pick a random instance
        int randomInstanceNumber = new Random().nextInt(members.size());
        Member randomMember = members.toArray(new Member[0])[randomInstanceNumber];

        // Return the random instance's UUID.
        return randomMember.getUuid();
    }

    /**
     * @return the ID of the current instance, or 'no-cluster' if clustering is disabled.
     */
    public String getLocalInstanceId() {
        if (instance == null) {
            return "no-cluster";
        }
        return instance.getCluster().getLocalMember().getUuid();
    }

}