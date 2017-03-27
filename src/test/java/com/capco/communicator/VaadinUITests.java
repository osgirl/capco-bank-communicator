package com.capco.communicator;

import javax.annotation.PostConstruct;

import com.capco.communicator.repository.BankRepository;
import com.capco.communicator.schema.Bank;
import com.capco.communicator.view.component.BankEditor;
import com.capco.communicator.view.ApplicationUI;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.boot.VaadinAutoConfiguration;

import static org.assertj.core.api.BDDAssertions.then;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VaadinUITests.Config.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class VaadinUITests {

    @Autowired
    BankRepository repository;
    VaadinRequest vaadinRequest = Mockito.mock(VaadinRequest.class);
    BankEditor editor;
    ApplicationUI ApplicationUI;

    @Before
    public void setup() {
        this.editor = new BankEditor(this.repository);
//        this.LoginUI = new LoginUI(this.repository, editor);
    }

    @Test
    public void shouldInitializeTheGridWithBankRepositoryData() {
        int BankCount = (int) this.repository.count();

        ApplicationUI.init(this.vaadinRequest);

//        then(LoginUI.grid.getColumns()).hasSize(3);
//        then(LoginUI.grid.getContainerDataSource().getItemIds()).hasSize(BankCount);
    }

    @Test
    public void shouldFillOutTheGridWithNewData() {
        int initialBankCount = (int) this.repository.count();
        this.ApplicationUI.init(this.vaadinRequest);
        BankDataWasFilled(editor, "Marcin", "Grzejszczak");

        this.editor.save.click();

//        then(LoginUI.grid.getContainerDataSource().getItemIds()).hasSize(initialBankCount + 1);
//        then((Bank) LoginUI.grid.getContainerDataSource().lastItemId())
//                .extracting("code", "name")
//                .containsExactly("Marcin", "Grzejszczak");
    }

    @Test
    public void shouldInitializeWithInvisibleEditor() {
        this.ApplicationUI.init(this.vaadinRequest);

        then(this.editor.isVisible()).isFalse();
    }

    @Test
    public void shouldMakeEditorVisible() {
        this.ApplicationUI.init(this.vaadinRequest);
//        Object itemId = this.LoginUI.grid.getContainerDataSource().getItemIds().iterator().next();
//
//        this.LoginUI.grid.select(itemId);

        then(this.editor.isVisible()).isTrue();
    }

    private void BankDataWasFilled(BankEditor editor, String firstName, String lastName) {
        this.editor.code.setValue(firstName);
        this.editor.name.setValue(lastName);
        editor.editBank(new Bank(firstName, lastName, "empty"));
    }

    @Configuration
    @EnableAutoConfiguration(exclude = VaadinAutoConfiguration.class)
    static class Config {

        @Autowired BankRepository repository;

        @PostConstruct
        public void initializeData() {
            this.repository.save(new Bank("Jack", "Bauer", "channel"));
            this.repository.save(new Bank("Chloe", "O'Brian", "channel"));
            this.repository.save(new Bank("Kim", "Bauer", "channel"));
            this.repository.save(new Bank("David", "Palmer", "channel"));
            this.repository.save(new Bank("Michelle", "Dessler", "channel"));
        }

    }

}
