package kr.hkjin.jakestalker;

import java.util.Comparator;

import kr.hkjin.jakestalker.restapi.model.Repository;

/**
 * Created by hkjin81 on 2017. 4. 25..
 */

public class RepositoryComparator implements Comparator<Repository> {


    @Override
    public int compare(Repository o1, Repository o2) {
        if (o1.getStargazersCount() < o2.getStargazersCount()) {
            return 1;
        }
        else if (o1.getStargazersCount() == o2.getStargazersCount()) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
