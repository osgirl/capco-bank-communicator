package com.capco.communicator;

import javax.annotation.PostConstruct;

import org.junit.Before;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VaadinUITests.Config.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class VaadinUITests {

    @Autowired
    BankRepository repository;
    VaadinRequest vaadinRequest = Mockito.mock(VaadinRequest.class);
    BankEditor editor;
    VaadinUI vaadinUI;

    @Before
    public void setup() {
        this.editor = new BankEditor(this.repository);
        this.vaadinUI = new VaadinUI(this.repository, editor);
    }

    @Test
    public void shouldInitializeTheGridWithBankRepositoryData() {
        int BankCount = (int) this.repository.count();

        vaadinUI.init(this.vaadinRequest);

        then(vaadinUI.grid.getColumns()).hasSize(3);
        then(vaadinUI.grid.getContainerDataSource().getItemIds()).hasSize(BankCount);
    }

    @Test
    public void shouldFillOutTheGridWithNewData() {
        int initialBankCount = (int) this.repository.count();
        this.vaadinUI.init(this.vaadinRequest);
        BankDataWasFilled(editor, "Marcin", "Grzejszczak");

        this.editor.save.click();

        then(vaadinUI.grid.getContainerDataSource().getItemIds()).hasSize(initialBankCount + 1);
        then((Bank) vaadinUI.grid.getContainerDataSource().lastItemId())
                .extracting("code", "name")
                .containsExactly("Marcin", "Grzejszczak");
    }

    @Test
    public void shouldInitializeWithInvisibleEditor() {
        this.vaadinUI.init(this.vaadinRequest);

        then(this.editor.isVisible()).isFalse();
    }

    @Test
    public void shouldMakeEditorVisible() {
        this.vaadinUI.init(this.vaadinRequest);
        Object itemId = this.vaadinUI.grid.getContainerDataSource().getItemIds().iterator().next();

        this.vaadinUI.grid.select(itemId);

        then(this.editor.isVisible()).isTrue();
    }

    private void BankDataWasFilled(BankEditor editor, String firstName, String lastName) {
        this.editor.code.setValue(firstName);
        this.editor.name.setValue(lastName);
        editor.editBank(new Bank(firstName, lastName));
    }

    @Configuration
    @EnableAutoConfiguration(exclude = VaadinAutoConfiguration.class)
    static class Config {

        @Autowired BankRepository repository;

        @PostConstruct
        public void initializeData() {
            this.repository.save(new Bank("Jack", "Bauer"));
            this.repository.save(new Bank("Chloe", "O'Brian"));
            this.repository.save(new Bank("Kim", "Bauer"));
            this.repository.save(new Bank("David", "Palmer"));
            this.repository.save(new Bank("Michelle", "Dessler"));
        }

    }

}
