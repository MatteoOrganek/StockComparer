package owres.stockcomparer.model.graph;

public class Profile implements IProfile {
    IProfile profile;

    private String name;
    private String email;
    private Watchlist watchlist;
    private Indicator indicator;

    public Profile(IProfile profile, String name, String email, Watchlist watchlist, Indicator indicator) {
        this.profile = profile;
        this.name = name;
        this.email = email;
        this.watchlist = watchlist;
        this.indicator = indicator;
    }

    public IProfile getProfile() {
        return profile;
    }

    public void setProfile(IProfile profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Watchlist getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }
}
