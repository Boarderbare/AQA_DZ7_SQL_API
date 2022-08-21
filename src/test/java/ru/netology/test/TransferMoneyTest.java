package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.rest.RestHelper;

public class TransferMoneyTest {

        @Test
        void shouldTransferFromFirstCardToSecond() {

            RestHelper rest = new RestHelper();
            rest.login();
            rest.getToken();

        }
    }

