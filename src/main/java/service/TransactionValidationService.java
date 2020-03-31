package service;

import model.Transaction;
import org.iban4j.IbanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rabbit.QueueProducer;
import util.CnpUtil;

@Service
public class TransactionValidationService {

    @Autowired
    private QueueProducer queueProducer;

    public void sendTransactionToPersistence(Transaction transaction) throws Exception {
        isValidTransaction(transaction);
        queueProducer.produce(transaction);
    }

    private void isValidTransaction(Transaction transaction) {
        validateName(transaction.getPayer().getName(), "Payer");
        validateName(transaction.getPayee().getName(), "Payee");
        validateDuplications(transaction);
        CnpUtil.validate(String.valueOf(transaction.getPayer().getCnp()));
        CnpUtil.validate(String.valueOf(transaction.getPayee().getCnp()));
        IbanUtil.validate(transaction.getPayer().getIban());
        IbanUtil.validate(transaction.getPayee().getIban());
    }

    private void validateDuplications(Transaction transaction) {
        if(transaction.getPayer().getCnp() == transaction.getPayee().getCnp()) {
            throw new IllegalArgumentException("Payer and Payee cannot have the same CNP!");
        }
        if(transaction.getPayer().getIban().equals(transaction.getPayee().getIban())) {
            throw new IllegalArgumentException("Payer and Payee cannot have the same IBAN!");
        }
    }

    private void validateName(String name, String role) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(role + "'s name cannot be empty!");
        }
    }
}
