package ru.kolomiec.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.kolomiec.database.dao.AuthTokenDAO;
import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.database.entity.Person;
import ru.kolomiec.dto.PersonDTO;
import ru.kolomiec.requests.AuthApiRequest;

public class AuthenticationCommand extends AbstractCommand{

    private final PersonDAO personDAO;
    private final AuthApiRequest authApiRequest;

    private final AuthTokenDAO authTokenDAO;

    public AuthenticationCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
        this.personDAO = new PersonDAO();
        this.authApiRequest = new AuthApiRequest();
        this.authTokenDAO = new AuthTokenDAO();
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        Person person = personDAO.findPersonByChatId(chat.getId());
        AuthToken token = authApiRequest.loginPersonOnApi(new PersonDTO(person.getUsername(), person.getPassword()));
        personDAO.savePerson(person, token);
        sendMessage(absSender, "your token is saved in db, you are authenticated", chat.getId().toString());
    }
}
