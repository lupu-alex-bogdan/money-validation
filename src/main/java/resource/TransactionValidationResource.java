package resource;

import io.swagger.annotations.ApiOperation;
import model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TransactionValidationService;

@RestController
@RequestMapping("/api")
public class TransactionValidationResource {

    @Autowired
    private TransactionValidationService transactionValidationService;

    @PostMapping("/transaction")
    @ApiOperation(value = "Persists a specific transaction",
            notes = "Provide a transaction to persist. It will be persisted only if it's valid!")
    ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) throws Exception {
        transactionValidationService.sendTransactionToPersistence(transaction);
        return new ResponseEntity<>(null, HttpStatus.CREATED) ;
    }
}
