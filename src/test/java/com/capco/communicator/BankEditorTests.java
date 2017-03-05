package com.capco.communicator;

import com.capco.communicator.schema.Bank;
import com.capco.communicator.view.component.BankEditor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.argThat;

@RunWith(MockitoJUnitRunner.class)
public class BankEditorTests {

    private static final String BANK_CODE = "BANK_CODE";
    private static final String BANK_NAME = "BANK_NAME";

    @Mock
    com.capco.communicator.repository.BankRepository BankRepository;
    @InjectMocks
    BankEditor editor;

    @Test
    public void shouldStoreBankInRepoWhenEditorSaveClicked() {
        this.editor.code.setValue(BANK_CODE);
        this.editor.name.setValue(BANK_NAME);
        BankDataWasFilled();

        this.editor.save.click();

        then(this.BankRepository).should().save(argThat(BankMatchesEditorFields()));
    }

    @Test
    public void shouldDeleteBankFromRepoWhenEditorDeleteClicked() {
        this.editor.code.setValue(BANK_CODE);
        this.editor.name.setValue(BANK_NAME);
        BankDataWasFilled();

        editor.delete.click();

        then(this.BankRepository).should().delete(argThat(BankMatchesEditorFields()));
    }

    private void BankDataWasFilled() {
        this.editor.editBank(new Bank(BANK_CODE, BANK_NAME));
    }

    private TypeSafeMatcher<Bank> BankMatchesEditorFields() {
        return new TypeSafeMatcher<Bank>() {
            @Override public void describeTo(Description description) {

            }

            @Override protected boolean matchesSafely(Bank item) {
                return BANK_CODE.equals(item.getCode()) &&
                        BANK_NAME.equals(item.getName());
            }
        };
    }

}
