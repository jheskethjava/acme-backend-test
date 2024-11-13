# acme-backend-test
September 2024

Hey, thanks for reading this!

My application uses Spring Boot and can be run from the command line using Maven. I've tested this on a shell terminal (zsh) so if you're using another type you may need to modify the commands so they work in your terminal.

If you're happy to go with the default values that were supplied in the test instructions then you can run this application with:  
  `./mvnw spring-boot:run`

This is all set up ready to go with:
  `acme.base-url=http://localhost:8070`  
  `acme.library-id=manchester-central`  
  `acme.loan-start-date-minimum=2022-01-01T00:00:00.000Z`  
  `acme.api-key` = the key you gave me

You can change these values by supplying run arguments on the command line like this:  
  `./mvnw spring-boot:run -Dspring-boot.run.arguments="--acme.library-id=<your library id here> --acme.base-url=<your base url here>"`

eg  
  `./mvnw spring-boot:run -Dspring-boot.run.arguments="--acme.library-id=bodlean --acme.base-url=https://api.acme.systems/interview-tests-mock-api/v1"`

Run tests with:  
  `./mvnw clean test`

If you want to pull down both projects locally and spin up my mock server then you can hit it by setting the acme.base-url to http://localhost:8070

Update: I'm new to GitHub but I've discovered codespaces. I've created one for the acme-backend-test project ('cuddly guacamole') and run the application successfully from it so that is an alternative to pulling down the code locally.
