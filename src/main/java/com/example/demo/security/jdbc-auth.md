### How To set up jdbc auth ***(using default table schemas)***

---

1. you should have the following tables in your ***Database***
   1. users
   2. authorities or roles
   3. **make sure to include password and role in those two tables only not other**

### sql script
```sql
-- password varchar(68) --> 60 for hashed value and 8 for {bcrypt}
CREATE TABLE users (
    username varchar(50) primary key,
    password varchar(68) not null,
    enabled tinyint(1)
);

CREATE Table authorities (
    username varchar(50),
    authority varchar(50) not null,
    UNIQUE KEY un_key (username,authority)
    FOREIGN KEY(username) references users(username)
);
-- {bcrypt} hashes the password.
-- {noop} plain text the password.
-- password is "12345"
INSERT INTO users VALUES 
    ("sam","{bcrypt}$2a$12$BMbal/YHJ97DP/xUcJ1RP.QBHdhznbJmVvDAaCdKMaGEQun41aN6.",1),
    ("sally","{bcrypt}$2a$12$BMbal/YHJ97DP/xUcJ1RP.QBHdhznbJmVvDAaCdKMaGEQun41aN6.",1),
    ("clara","{bcrypt}$2a$12$BMbal/YHJ97DP/xUcJ1RP.QBHdhznbJmVvDAaCdKMaGEQun41aN6.",1);

-- spring uses `Role_` to configure roles
INSERT INTO authorities VALUES
    ("sam","ROLE_ADMIN"),
    ("sally","ROLE_MANAGEER"),
    ("clara","ROLE_EMPLOYEE");


```
---
### How To set up jdbc auth ***(using custom table schemas)***

1. Provide query to fid user by username or email
2. provide query to find authorities

### In @Configuration class add  this:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Bean
public UserDetailsManager userDetailsManager(DataSource dataSource) {
    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
    manager.setUsersByUsernameQuery("select user_id,email,password,active from employees where user_id=?");
    manager.setAuthoritiesByUsernameQuery("select user_id,role from roles where user_id=?");
    return manager;
}
```
