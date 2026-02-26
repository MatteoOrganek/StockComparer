package owres.stockcomparer.model.profile;

import owres.stockcomparer.model.profile.infrastructure.ProfileApiBridge;

public class ProfileServ {

    private IProfileService profileService;

    public ProfileServ() {
        profileService = new ProfileApiBridge();
    }

    public String getProfile(String symbol) {
        return profileService.getProfileData(symbol);
    }
}