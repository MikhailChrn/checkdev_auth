package ru.checkdev.auth.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.dto.EmailCheckResponse;
import ru.checkdev.auth.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.Optional;

/**
 * @author parsentev
 * @since 26.09.2016
 */
@RestController
public class AuthController {
    private final PersonService persons;
    private final String ping = "{}";

    @Autowired
    public AuthController(final PersonService persons) {
        this.persons = persons;
    }

    /**
     * Аннотация @RequestMapping используется для создания простого универсального эндпоинта,
     * который быстро возвращает данные аутентифицированного пользователя
     * без привязки к конкретному HTTP-методу.
     * Аннотация @RequestMapping("/user") применяется только к тому методу,
     * над которым она объявлена, и не создает никакого общего префикса для других методов класса.
     */
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping("/ping")
    public String ping() {
        return this.ping;
    }

    /**
     *  Метод не используется в настоящий момент.
     *  Пользователь сразу активируется при регистрации.
     */
    @GetMapping("/auth/activated/{key}")
    public Object activated(@PathVariable String key) {
        if (this.persons.activated(key)) {
            return new Object() {
                public boolean getSuccess() {
                    return true;
                }
            };
        } else {
            return new Object() {
                public String getError() {
                    return "Notify has already activated";
                }
            };
        }
    }

    /**
     * Регистрация нового пользователя
     * Обязательные поля
     * {
     *      "email": "ivanov@example.com",
     *      "password": "123456",
     *      "privacy": true
     * }
     */
    @PostMapping("/registration")
    public Object registration(@RequestBody Profile profile) {
        Optional<Profile> result = this.persons.reg(profile);
        return result.<Object>map(prs -> new Object() {
            public Profile getPerson() {
                return prs;
            }
        }).orElseGet(() -> new Object() {
            public String getError() {
                return String.format("Пользователь с почтой %s уже существует.", profile.getEmail());
            }
        });
    }

    /**
     * Восстановление пароля, сервис генерирует новый пароль.
     * Использовать нельзя т.к не работает оповещение.
     * Пароль не удаётся получить.
     */
    @PostMapping("/forgot")
    public Object forgot(@RequestBody Profile profile) {
        Optional<Profile> result = this.persons.forgot(profile);
        if (result.isPresent()) {
            return new Object() {
                public String getOk() {
                    return "ok";
                }
            };
        } else {
            return new Object() {
                public String getError() {
                    return "E-mail не найден.";
                }
            };
        }
    }

    /**
     * Метод пуст.
     */
    @GetMapping("/revoke")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) {

    }
}
