git pull
timeout /t 10 /nobreak
gradlew bootrun --args='--spring.profiles.active=mono'