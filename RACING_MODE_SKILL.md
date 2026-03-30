# Racing Mode Database Skill

## Context
This skill provides a comprehensive racing mode database management system. The application manages a racing fleet with vehicles, pilots, garages, categories, championships, and tracks.

## Database Schema

### Tables

#### 1. **Categories**
Defines the different racing categories/classes for vehicles.
```sql
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    image VARCHAR(255)
);
```

#### 2. **Pilots**
Information about race drivers.
```sql
CREATE TABLE pilot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    license VARCHAR(255),
    image VARCHAR(255),
    experience INT,
    garage_id BIGINT,
    FOREIGN KEY (garage_id) REFERENCES garage(id)
);
```

#### 3. **Garages**
Pit stops and maintenance facilities for vehicles and pilots.
```sql
CREATE TABLE garage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    credits BIGINT DEFAULT 0
);
```

#### 4. **Cars**
Race vehicles in the system.
```sql
CREATE TABLE car (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    price BIGINT,
    model BIGINT,
    image VARCHAR(255),
    category_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id)
);
```

#### 5. **Tracks**
Racing circuits where championships take place.
```sql
CREATE TABLE track (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255),
    image VARCHAR(255),
    number_of_layouts INT
);
```

#### 6. **Championship** 
Competitions consisting of multiple tracks and specific categories.
```sql
CREATE TABLE championship (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image VARCHAR(255),
    total_prize BIGINT DEFAULT 0,
    number_of_tracks INT,
    entry_fee BIGINT DEFAULT 0
);
``` 

#### 7. **ChampionshipCategory**
Defines which categories can participate in which championships (Many-to-Many).
```sql
CREATE TABLE championship_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    championship_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (championship_id) REFERENCES championship(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);
```

#### 8. **GarageChampionship**
Tracks participation of garages in championships.
```sql
CREATE TABLE garage_championship (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    garage_id BIGINT NOT NULL,
    championship_id BIGINT NOT NULL,
    status VARCHAR(50), -- e.g., "ENTERED", "COMPLETED"
    FOREIGN KEY (garage_id) REFERENCES garage(id),
    FOREIGN KEY (championship_id) REFERENCES championship(id)
);
```

#### 9. **GarageCar**
Intermediate table for Many-to-Many relationship between Garages and Cars.
```sql
CREATE TABLE garage_car (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    car_id BIGINT NOT NULL,
    garage_id BIGINT NOT NULL,
    FOREIGN KEY (car_id) REFERENCES car(id),
    FOREIGN KEY (garage_id) REFERENCES garage(id)
);
```

## Features

### 1. Track Management
- Register tracks with name, country, and number of available layouts.
- List and manage tracks via `TrackController`.

### 2. Championship Management
- Create championships with specific prize pools, entry fees, and number of tracks.
- **Auto-fill Tracks Feature**: In the championship form, a button allows generating a random race calendar in the description based on the `numberOfTracks`. It fetches all available tracks and randomly selects layouts.
- Calculate earnings based on placement: ChampionshipService#calculateSpecialWin
  Place | Percentage
  * 1.º Place	100%
  * 2.º Place	12.9%
  * 3.º Place	40% 
  * 4º Place	24%	
  * 5º Place	16%	
  * 6º Place	12%	
  * 7º Place	10%	
  * 8º Place	8%	
  * 9º Place	6%	
  * 10º Place	4%

### 3. Pilot & Garage Management
- Manage pilots and their assignments to garages.
- Track garage credits and championship participation.

### 4. Car & Category Management
- Categorize cars and manage their distribution across garages.

## API Endpoints

### Tracks (REST)
- `GET /tracks/api` - List all tracks (used by auto-fill feature)

### Championships (REST)
- `GET /championships/api` - List all championships
- `GET /championships/api/{id}` - Get championship details
- `POST /championships/api` - Create championship
- `PUT /championships/api/{id}` - Update championship
- `DELETE /championships/api/{id}` - Delete championship
- `GET /championships/api/{id}/calculate-earnings/{placement}` - Earnings calculator

### Web Controllers (UI)
- `CarController`: `/cars`, `/cars/new`, `/cars/{id}/edit`
- `CategoryController`: `/categories`
- `ChampionshipController`: `/championships`, `/championships/new`, `/championships/{id}/edit`
- `GarageController`: `/garages`
- `PilotController`: `/pilots`
- `TrackController`: `/tracks`, `/tracks/new`, `/tracks/{id}/edit`

## Views (Thymeleaf Templates)

- `championship-form.html`: Form to create/edit championships. Includes "Auto-fill Tracks" JS logic.
- `championships-list.html`: Grid view of all available competitions.
- `tracks-list.html`: Management view for racing circuits.
- `menu.html`: Main navigation hub.

## Data Models (Java Entities)

### Track Entity
```java
@Entity
@Data
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String country;
    private String image;
    private Integer numberOfLayouts;
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
    private Integer numberOfTracks;
    private Long entryFee;
    
    @OneToMany(mappedBy = "championship")
    private List<ChampionshipCategory> championshipCategories;
}
```

## Technical Stack
- **Framework**: Spring Boot 3.4.1
- **Language**: Java 21
- **Database**: H2 (embedded) / `career_mode_db.mv.db`
- **Template Engine**: Thymeleaf
- **ORM**: Spring Data JPA
- **Build Tool**: Maven

---
**Racing Mode Skill v1.1** - Updated with Tracks, Advanced Championship fields, and Auto-fill functionality.
