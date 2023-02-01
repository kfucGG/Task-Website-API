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


public class RegistrationCommand extends AbstractCommand {

    private final PersonDAO personDAO;
    private final AuthApiRequest authApiRequest;

    private final AuthTokenDAO authTokenDAO;
    public RegistrationCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
        this.personDAO = new PersonDAO();
        this.authApiRequest = new AuthApiRequest();
        this.authTokenDAO = new AuthTokenDAO();
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        Person person = buildPersonFromArrayOfStringAndChatId(strings, chat);
        personDAO.savePerson(person);
        AuthToken token = authApiRequest.registrationNewPersonOnApi(new PersonDTO(person.getUsername(), person.getPassword()));
        authTokenDAO.saveAuthTokenToPersonByPersonChatId(token, chat.getId());
        sendMessage(absSender, "your token is saved in db, you are registered", chat.getId().toString());
    }
}
