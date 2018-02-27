package Practice5.pr5.spi;

import static com.google.devrel.training.conference.service.OfyService.ofy;

import javax.persistence.Entity;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import Practice5.pr5.*;
import Practice5.pr5.domain.Profile;
import Practice5.pr5.form.ProfileForm;
import Practice5.pr5.form.ProfileForm.TeeShirtSize;

import com.googlecode.objectify.Key;

/**
 * Defines conference APIs.
 */
@Api(name = "conference", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
        Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID }, description = "API for the Conference Central Backend application.")
public class ConferenceApi {

    /*
     * Get the display name from the user's email. For example, if the email is
     * lemoncake@example.com, then the display name becomes "lemoncake."
     */
    private static String extractDefaultDisplayNameFromEmail(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }

    /**
     * Creates or updates a Profile object associated with the given user
     * object.
     *
     * @param user
     *            A User object injected by the cloud endpoints.
     * @param profileForm
     *            A ProfileForm object sent from the client form.
     * @return Profile object just created.
     * @throws UnauthorizedException
     *             when the User object is null.
     */

    // Declare this method as a method available externally through Endpoints
    @ApiMethod(name = "saveProfile", path = "profile", httpMethod = HttpMethod.POST)
    // The request that invokes this method should provide data that
    // conforms to the fields defined in ProfileForm

    public Profile saveProfile(ProfileForm profile_form, User user) throws UnauthorizedException {

        String userId = null;
        String mainEmail = null;
        String displayName = "Your name will go here";
        TeeShirtSize teeShirtSize = TeeShirtSize.NOT_SPECIFIED;

        // If the user is not logged in, throw an UnauthorizedException
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // Set the teeShirtSize to the value sent by the ProfileForm, if sent
        // otherwise leave it as the default value
        if (profile_form.getTeeShirtSize() != null) {
            teeShirtSize = profile_form.getTeeShirtSize();
        }

        // Set the displayName to the value sent by the ProfileForm, if sent
        // otherwise set it to null
        if(profile_form.getDisplayName() != null) {
        	displayName = profile_form.getDisplayName();
        }

        // TODO 2
        // Get the userId and mainEmail
        //userId = user.getId();
        mainEmail = user.getEmail();

        // If the displayName is null, set it to default value based on the user's email
        // by calling extractDefaultDisplayNameFromEmail(...)
        if(displayName == null) {
        	displayName = extractDefaultDisplayNameFromEmail(mainEmail);
        }

        Profile profile = getProfile(user);
        if (profile == null)
        profile = new Profile(userId, displayName, mainEmail, teeShirtSize);
        else
        profile.update(displayName,teeShirtSize);

        ofy().save().entity(profile).now();
        
        // Return the profile
        return profile;
    }

    /**
     * Returns a Profile object associated with the given user object. The cloud
     * endpoints system automatically inject the User object.
     *
     * @param user
     *            A User object injected by the cloud endpoints.
     * @return Profile object.
     * @throws UnauthorizedException
     *             when the User object is null.
     */
    @ApiMethod(name = "getProfile", path = "profile", httpMethod = HttpMethod.GET)
    public Profile getProfile(final User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // TODO
        // load the Profile Entity
        String userId = user.getId(); // TODO
        Key key = Key.create(Entity.class, userId); // TODO
        Profile profile = (Profile)ofy().load().key(key).now(); // TODO load the Profile entity
        return profile;
    }
}
