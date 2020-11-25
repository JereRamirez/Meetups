**MEETUPS
(Java + SpringBoot)

API to manage your meetups

Swagger: http://localhost:8080/swagger-ui.html
To test endpoints through Postman you need to provide "Basic Auth" in the Authorization tab. Roles authorizations can be found in the "SecurityConfiguration" class.


This application enables you to get information about your meetups.
Currentyl available features:

  - Meetup ABM
  - Add attendees to an existing Meetup
  - Chek-in user to a Meetup
  - Calculate "Pack de Birras" needed to get for a Meetup based on the weather and the attendees confirmed.
  - Get forecast for the Meetup (Through an external weather api, the result is cached for future calls)
  - Clean cache for Meetup's temperatures

