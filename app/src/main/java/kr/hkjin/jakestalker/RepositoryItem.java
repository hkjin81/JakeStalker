package kr.hkjin.jakestalker;

/**
 * Created by hkjin81 on 2017. 4. 25..
 */

public class RepositoryItem {
    public String imageUrl = "";
    public String title = "Title";
    public String description = "Description";
    private String starCountText = "Star Count : 0";
    private int starCount = 0;

    public void setStarCount(int count) {
        starCount = count;
        starCountText = String.format("Star Count : %d", starCount);
    }

    public int getStarCount() {
        return starCount;
    }

    public String getStarCountText() {
        return starCountText;
    }


}
