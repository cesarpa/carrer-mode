# Racing Mode Database Skill

## Context
This skill provides a comprehensive racing mode database management system. The application manages a racing fleet with vehicles, pilots, garages, and vehicle categories.

## Database Schema

### Tables

#### 1. **Categories**
Defines the different racing categories/classes for vehicles.
```sql
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    image VARCHAR(255) NOT NULL UNIQUE
);
```
**Fields:**
- `id`: Unique identifier
- `name`: Category name (e.g., "Formula 1", "GT", "Truck Racing")

#### 2. **Pilots**
Information about race drivers.
```sql
CREATE TABLE pilot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    license VARCHAR(255)
    garage_id BIGINT,
    experience INT
);
```
**Fields:**
- `id`: Unique identifier
- `first_name`: Driver's first name
- `last_name`: Driver's last name
- `id_card`: National ID card number (unique)
- `date_of_birth`: Driver's birth date
- `nationality`: Driver's nationality
- `years_of_experience`: Years of racing experience
- `license`: Racing license type

#### 3. **Garages**
Pit stops and maintenance facilities for vehicles.
```sql
CREATE TABLE garage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);
```
**Fields:**
- `id`: Unique identifier
- `name`: Garage name

#### 4. **Cars**
Race vehicles in the system.
```sql
CREATE TABLE car (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    price BIGINT,
    model BIGINT,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id)
);
```
**Fields:**
- `id`: Unique identifier
- `name`: Car name/model (e.g., "McLaren F1", "Ferrari F50")
- `price`: Car price in dollars
- `model`: Model code/number
- `category_id`: Reference to category (required)

#### 5. **GarageCar**
Intermediate table for Many-to-Many relationship between Garages and Cars.
Allows a car to be present in multiple garages.
```sql
CREATE TABLE garage_car (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    car_id BIGINT NOT NULL,
    garage_id BIGINT NOT NULL,
    FOREIGN KEY (car_id) REFERENCES car(id),
    FOREIGN KEY (garage_id) REFERENCES garage(id)
);
```

### 6. **Championship** 
```sql
CREATE TABLE championship (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image VARCHAR(255),
    total_prize BIGINT DEFAULT 0
);
``` 

### 7. **ChampionshipCategory**
Defines which categories can participate in which championships.
```sql
CREATE TABLE championship_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    championship_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (championship_id) REFERENCES championship(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);
```

## Features

### 1. Category Management
- Create new racing categories
- List all categories
- Update category details
- Delete categories

### 2. Pilot Management
- Register new racing pilots
- Store pilot information (name, country)
- Track pilot experience and licenses // in the future, we can expand this to include more detailed information
- Manage pilot assignments to vehicles

### 3. Garage Management
- Store cars information

### 4. Car Management
- Create cars
- Assign cars categories
- Assign cars to garages
- Track car pricing and models
- Edit car details
- Delete cars from system
- View all cars in inventory

### 5. Championship Management
- Create new championships
- Assign categories to championships
- Calculate earnings based on placements in championships (using the following distribution):
  Place | Percentaje
  * 1.º Place	14%
  * 2.º Place	12.9%
  * 3.º Place	11.6%
  * 4.º Place	10.2%
  * 5.º Place	9%
  * 6.º Place	8.1%
  * 7.º Place	7.1%
  * 8.º Place	6.1%
  * 9.º Place	5.1%
  * 10.º Place	4%
  * 11 and more	0%


## API Endpoints

### Categories
- `GET /api/v1/categorias` - List all categories
- `GET /api/v1/categorias/{id}` - Get category by ID
- `GET /api/v1/categorias/nombre/{name}` - Get category by name
- `POST /api/v1/categorias` - Create new category
- `PUT /api/v1/categorias/{id}` - Update category
- `DELETE /api/v1/categorias/{id}` - Delete category

### Pilots
- `GET /api/v1/pilotos` - List all pilots
- `GET /api/v1/pilotos/{id}` - Get pilot by ID
- `GET /api/v1/pilotos/cedula/{idCard}` - Get pilot by ID card
- `POST /api/v1/pilotos` - Create new pilot
- `PUT /api/v1/pilotos/{id}` - Update pilot
- `DELETE /api/v1/pilotos/{id}` - Delete pilot

### Garages
- `GET /api/v1/garages` - List all garages
- `POST /api/v1/garages` - Create new garage
- `PUT /api/v1/garages/{id}` - Update garage
- `DELETE /api/v1/garages/{id}` - Delete garage

### Cars (REST API)
- `GET /cars/api` - List all cars
- `GET /cars/api/{id}` - Get car by ID
- `POST /cars/api` - Create new car
- `PUT /cars/api/{id}` - Update car
- `DELETE /cars/api/{id}` - Delete car

### Cars (Web UI)
- `GET /` - Dashboard with all cars
- `GET /cars` - Car list page
- `GET /cars/new` - Add car form
- `POST /cars` - Save new car
- `GET /cars/{id}/edit` - Edit car form
- `POST /cars/{id}` - Update car
- `GET /cars/{id}/delete` - Delete car

### Championships
- `GET /api/v1/championships` - List all championships
- `GET /api/v1/championships/{id}` - Get championship by ID
- `POST /api/v1/championships` - Create new championship
- `PUT /api/v1/championships/{id}` - Update championship
- `DELETE /api/v1/championships/{id}` - Delete championship
- `GET /api/v1/championships/{id}/calculate-earnings/{placement}` - Calculate earnings for a placement in a championship

## Web Interface

### Dashboard (`/`)
Main page showing all racing cars in an attractive grid layout with:
- Car name and details
- Category badge
- Garage location
- Action buttons (Edit, Delete)

### Add/Edit Car (`/cars/new` and `/cars/{id}/edit`)
Form to create or modify vehicle with fields:
- Car name
- Model code
- Price
- Category selection
- Garage selection

## Technical Stack
- **Framework**: Spring Boot 4.0.5
- **Language**: Java 21
- **Database**: H2 (embedded)
- **Template Engine**: Thymeleaf
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **API Documentation**: SpringDoc OpenAPI (Swagger)

## Data Models

### Category Entity
```java
@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
}
```

### Pilot Entity
```java
@Entity
@Data
public class Pilot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String country;
    private String licence;
    private String image;
    private Long experience;
    private Garage garage;
}
```

### GarageCar Entity
```java
@Entity
@Data
public class GarageCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Car car;
    
    @ManyToOne
    private Garage garage;
}
```

### Garage Entity
```java
@Entity
@Data
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long credits;
    
    @OneToMany(mappedBy = "garage")
    private List<GarageCar> garageCars;
}
```

### Car Entity
```java
@Entity
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;
    private Long model;
    private String image;
    
    @ManyToOne
    private Category category;
    
    @OneToMany(mappedBy = "car")
    private List<GarageCar> garageCars;
}
```

### Championship Entity
```java
@Entity
@Data
public class Championship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    private Long totalPrize;
    
    @OneToMany(mappedBy = "championship")
    private List<ChampionshipCategory> championshipCategories;
}
```

### ChampionshipCategory Entity
```java
@Entity
@Data
public class ChampionshipCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Championship championship;
    
    @ManyToOne
    private Category category;
}
```

## Services

- **CategoryService**: CRUD operations for categories
- **PilotService**: CRUD operations for pilots
- **GarageService**: CRUD operations for garages
- **CarService**: CRUD operations for cars with filtering capabilities

## Getting Started

### Access the Application
1. Start the application: `mvn spring-boot:run`
2. Open browser to `http://localhost:8080`
3. Navigate using the main menu

### Create a Racing Category
```bash
curl -X POST http://localhost:8080/api/v1/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Formula 1",
    "description": "Single-seater racing",
    "displacement": 1000,
    "maxPower": 900
  }'
```

### Create a Garage
```bash
curl -X POST http://localhost:8080/api/v1/garages \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monaco Pit Stop",
    "location": "Monte Carlo",
    "capacity": 5,
    "contactPhone": "+377 1234567",
    "contactEmail": "info@monacopit.com"
  }'
```

### Add a Racing Car
```bash
curl -X POST http://localhost:8080/cars/api \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ferrari F1 2024",
    "price": 15000000,
    "model": 2024,
    "category": {"id": 1},
    "garage": {"id": 1}
  }'
```

## Typical Use Cases

1. **Register a new racing category** for an upcoming championship
2. **Create pit stops** in different locations for maintenance
3. **Register racing drivers** with their experience levels
4. **Add vehicles** to inventory with assigned categories and garages
5. **Track vehicle assignments** to pilots and locations
6. **Manage vehicle details** (pricing, specifications)
7. **View racing fleet** dashboard with all vehicles

## Database Initialization

The application uses H2 in-memory database. Data is persisted during the session but reset on application restart. For persistent storage, configure a production database in `application.properties`.

## Notes
- All APIs support CORS (Cross-Origin Resource Sharing)
- Responses use standard HTTP status codes
- Validation is enforced on required fields
- Category is mandatory for all cars
- Garage assignment is optional

---
**Racing Mode Skill v1.0** - Comprehensive car fleet management system for racing competitions.
