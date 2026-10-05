# TODO 6: package FreightBoard as a container image. Build the jar first (mvn -q package -pl sb10-production -DskipTests),
# then from this folder:  docker build -t freightboard .   and   docker run -p 8080:8080 freightboard
#
# Two stages. Stage 1 unpacks the jar into LAYERS: dependencies change rarely and the app's own classes change
# often, so rebuilding after a code change only replaces the small top layer.
#   - base image eclipse-temurin:21-jre, named "builder"; WORKDIR /app; COPY target/*.jar app.jar
#   - RUN java -Djarmode=tools -jar app.jar extract --layers --launcher --destination extracted
# Stage 2 is the image that actually runs:
#   - base image eclipse-temurin:21-jre; WORKDIR /app
#   - create an unprivileged user and switch to it (never run a server as root):
#       RUN useradd --system --create-home freightboard     then     USER freightboard
#   - COPY --from=builder each layer directory, in this order:
#       /app/extracted/dependencies/  /app/extracted/spring-boot-loader/  /app/extracted/snapshot-dependencies/
#       /app/extracted/application/          (each one to ./)
#   - EXPOSE 8080
#   - ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
