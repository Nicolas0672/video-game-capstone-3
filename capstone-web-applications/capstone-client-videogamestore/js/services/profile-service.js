let profileService;

class ProfileService
{
    loadProfile()
    {
        const url = `${config.baseUrl}/profile`;

        axios.get(url)
             .then(response => {

                 templateBuilder.build("profile", response.data, "main")
             })
             .catch(error => {
                 const data = {
                     error: "Load profile failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }

   updateProfile(profile)
   {
       const url = `${config.baseUrl}/profile`;

       axios.put(url, profile)
            .then(() => {
                const data = {
                    message: "The profile has been updated."
                };

                templateBuilder.append("message", data, "errors")
            })
            .catch(error => {
                let errorMessage = "Save profile failed.";

                // Check if it's a 400 error (email already exists)
                if (error.response && error.response.status === 400) {
                    errorMessage = "Email already exists. Please use a different email.";
                }

                const data = {
                    error: errorMessage
                };

                templateBuilder.append("error", data, "errors")
            })
   }
}

document.addEventListener("DOMContentLoaded", () => {
   profileService = new ProfileService();
});
