import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class DataCruncher {

    // do not modify this method - just use it to get all the Transactions that are in scope for the exercise
    public List<Transaction> readAllTransactions() throws Exception {
        return Files.readAllLines(Paths.get("src/main/resources/payments.csv"), StandardCharsets.UTF_8)
                .stream()
                .skip(1)
                .map(line -> {
                    var commaSeparatedLine = List.of(line
                            .replaceAll("'", "")
                            .split(",")
                    );
                    var ageString = commaSeparatedLine.get(2);
                    var ageInt = "U".equals(ageString) ? 0 : Integer.parseInt(ageString);
                    return new Transaction(commaSeparatedLine.get(1),
                            ageInt,
                            commaSeparatedLine.get(3),
                            commaSeparatedLine.get(4),
                            commaSeparatedLine.get(5),
                            commaSeparatedLine.get(6),
                            commaSeparatedLine.get(7),
                            Double.parseDouble(commaSeparatedLine.get(8)),
                            "1".equals(commaSeparatedLine.get(9)));
                })
                .collect(Collectors.toList());
    }

    // example
    public List<Transaction> readAllTransactionsAge0() throws Exception {
        return readAllTransactions().stream()
                .filter(transaction -> transaction.getAge() == 0)
                .collect(Collectors.toList());
    }

    // task 1
    public Set<String> getUniqueMerchantIds() throws Exception {
        Set<String> merchantIds = readAllTransactions().stream().map(transaction -> transaction.getMerchantId()).collect(Collectors.toSet());
        return merchantIds;
    }

    // task 2
    public long getTotalNumberOfFraudulentTransactions() throws Exception {
        return readAllTransactions().stream().filter(transaction -> transaction.isFraud()).count();
    }

    // task 3
    public long getTotalNumberOfTransactions(boolean isFraud) throws Exception {
        return readAllTransactions().stream().filter(transaction -> transaction.isFraud() == isFraud).count();
    }

    // task 4
    public Set<Transaction> getFraudulentTransactionsForMerchantId(String merchantId) throws Exception {
        return readAllTransactions().stream().filter(transaction -> transaction.isFraud() && transaction.getMerchantId().equals(merchantId)).collect(Collectors.toSet());
    }

    // task 5
    public Set<Transaction> getTransactionsForMerchantId(String merchantId, boolean isFraud) throws Exception {
        return readAllTransactions().stream().filter(transaction -> transaction.getMerchantId().equals(merchantId) && transaction.isFraud() == isFraud).collect(Collectors.toSet());
    }

    // task 6
    public List<Transaction> getAllTransactionsSortedByAmount() throws Exception {
       return readAllTransactions().stream().sorted(Comparator.comparing(transaction -> transaction.getAmount())).collect(Collectors.toList());
    }

    // task 7
    public double getFraudPercentageForMen() throws Exception {
        long numFraudForMen = readAllTransactions().stream().filter(transaction -> transaction.isFraud() && transaction.getGender().equals("M")).count();
        long numFrauds = readAllTransactions().stream().filter(transaction -> transaction.isFraud()).count();
        return (double)(numFraudForMen)/(double)(numFrauds);
    }

    // task 8
    public Set<String> getCustomerIdsWithNumberOfFraudulentTransactions(int numberOfFraudulentTransactions) throws Exception {
        Set<String> customerIds = readAllTransactions().stream().filter(Transaction::isFraud).collect(Collectors.groupingBy(Transaction::getCustomerId, Collectors.counting())).entrySet().stream()
                .filter(x->x.getValue() == numberOfFraudulentTransactions).map(x->x.getKey()).collect(Collectors.toSet());
        //readAllTransactions().stream().filter(x->x.getCustomerId().equals("C1947400039") && x.isFraud()).forEach(x->System.out.println(x.getAmount()));
    return customerIds;
    }

    // task 9
    public Map<String, Long> getCustomerIdToNumberOfTransactions() throws Exception {
        return readAllTransactions().stream().collect(Collectors.groupingBy(Transaction::getCustomerId, Collectors.counting()));
    }

    // task 10
    public Map<String, Double> getMerchantIdToTotalAmountOfFraudulentTransactions() throws Exception {
        return readAllTransactions().stream().filter(Transaction::isFraud).collect(Collectors.groupingBy(Transaction::getMerchantId, Collectors.summingDouble(Transaction::getAmount)));
    }

    // bonus
    public double getRiskOfFraudFigure(Transaction transaction) throws Exception {
        /*
        Ideas: Statistically learning/calculate the risk of fraud probability based on the weights of the factors(important features),
               factors include : age, gender, zipcode, category, amount. Assume it is linear regression relationship between the
               risk probability and the factors: Y= b0+w1x1+w2x2+...+w5x5. the result of the linear equation need to be
               applied with logit function to make the results fall between 0 and 1.

               So this is essentially logical regression machine learning model to predict the probability of the transaction being
               fraud. Using the dataset payments.csv with the fraud as the label , train the model, validation, test and
               evaluation the model, and then use the model to predict the transaction.

               I would prefer to use python/anaconda scikit-learn for the model creation, the key code snippet looks like:
               model = LogisticRegression().fit(X_train, Y_train), model.predict(X). after model is evaluated good enough,
               then make the model available as REST endpoint for JAVA/other to call with the transantion info,
               and the model will response with the probability of the fraudulence for the transanction.
         */
        return 1.0;
    }
}
