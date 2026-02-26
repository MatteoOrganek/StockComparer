package owres.stockcomparer.model.profile.infrastructure;

import owres.stockcomparer.model.profile.IProfileService;

public class ProfileApiBridge implements IProfileService {

    @Override
    public String getProfileData(String symbol) {
        return "Mock profile data for " + symbol;
    }
}