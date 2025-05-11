### You can run the app here... [TravelNest](https://travelnest-production.up.railway.app)
# TravelNest 🏡🚗✈️

TravelNest is a full-stack web application for booking accommodations, renting cars, and buying tickets for attractions and events. It supports both standard users and administrators, offering extensive CRUD functionality, filtering, authentication, and media upload.

---

## ⚙️ Tech Stack

- **Backend**: Java 17, Spring Boot, Spring Security, Spring Data JPA, JWT, REST
- **Frontend**: HTML5, CSS3, Bootstrap 5, Thymeleaf
- **Database**: MySQL
- **Build Tool**: Gradle
- **Others**: Imgur API for image upload, Flatpickr (calendar), Font Awesome, Lombok, SLF4J, Logback

---

## 🚀 Features

### 👤 Authentication & Authorization
- Secure login and registration with JWT tokens
- Role-based access: USER / ADMIN

### 🏠 Accommodations
- Add, edit, delete, and view housing listings
- Rent houses with validation of availability

### 🚗 Car Rentals
- Add, edit, delete, and rent cars
- Car rental period management

### 🎫 Attractions & Events
- View paid and free events
- Buy tickets with validation
- Admins can delete listings

### 🔎 Filtering & Search
- Filter by city, category, and more

### 🖼️ Image Upload
- Upload images for listings via Imgur API

### 🌍 Multilingual support
- Dynamic message source using Spring `Messages.properties`

---

## 🧪 Testing
- Includes basic validation and service-level testing

---

## 🔧 Running Locally

### Prerequisites
- Java 17+
- Gradle
- MySQL

### Clone the Repository

```bash
git clone https://github.com/yourusername/travel-nest.git
cd travel-nest
```

### Set up MySQL
Create a database named `travel_nest` and configure your credentials in `application.properties`.

### Run the App

```bash
./gradlew bootRun
```

Access it at `http://localhost:8080`

---

## 📂 Project Structure

```
src/
 ┣ main/
 ┃ ┣ java/bg/softuni/travelNest/
 ┃ ┣ resources/templates/       <-- Thymeleaf HTML templates
 ┃ ┗ resources/static/          <-- JS/CSS/Images
 ┗ test/
```

---

## 🙋‍♂️ Author

Developed by a SoftUni student as a full-stack graduation project. Built with care and passion for practical learning.

---

## 📜 License

This project is open-source and available under the MIT License.
