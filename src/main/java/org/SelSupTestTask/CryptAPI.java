package org.SelSupTestTask;

import java.io.Serializable;
import java.util.*;
import java.lang.Boolean;
import java.lang.String;
import java.util.List;

public class CryptAPI {
    public static void main(String[] args) {

        //Для проверки работоспособности создаем экземпляр класса
        CryptApiHandler cryptApiHandler = new CryptApiHandler(20, 2);

        //Создаем три документа
        DocumentEntity documentEntity1 = new DocumentEntity();
        DocumentEntity documentEntity2 = new DocumentEntity();
        DocumentEntity documentEntity3 = new DocumentEntity();

        //Заполняем документы, чтобы они различались
        documentEntity1.setOwner_inn("001");
        documentEntity2.setOwner_inn("002");
        documentEntity3.setOwner_inn("003");

        //Создаем подписи для передачи в метод createDocument
        String signature1 = "Документ 1";
        String signature2 = "Документ 2";
        String signature3 = "Документ 3";

        //Запускаем 3 потока, а лимит 2 запроса за 20 сек.
        new Thread(() -> cryptApiHandler.createDocument(documentEntity1, signature1)).start();
        new Thread(() -> cryptApiHandler.createDocument(documentEntity2, signature2)).start();
        new Thread(() -> cryptApiHandler.createDocument(documentEntity3, signature3)).start();
    }
}

/**
 * Класс для создания документа и ввода в оборот товара, произведенного в РФ.
 * Документ и подпись должны передаваться в метод в виде Java объекта и строки соответственно.
 */
class CryptApiHandler {
    private long timeUnit;
    private int requestLimit;
    private static int counter;
    private static long startTime;


    public CryptApiHandler(long timeUnit, int requestLimit) {

        //пределы времени и количества запросов должны быть больше ноля.
        if (timeUnit > 0 && requestLimit > 0) {
            this.timeUnit = timeUnit * 1000;
            this.requestLimit = requestLimit;
        }
        //начальное время записывается в переменную, только если счетчик запросов равен нулю.
        if (counter == 0) {
            startTime = System.currentTimeMillis();
        }
        counter++;
    }

    /**
     * Метод выполняет основную задачу тестового задания.
     * <ol>Работает метод следующим образом:
     * <li>Цикл while проверяет не превышает ли количество запросов в заданную единицу времени допустимых значений,
     * и если превышает, то цикл просто не выполняется. Соответственно и документ не возвращается из метода.
     * В таком случае метод возвращает null.</li>
     * <li>Условие if проверяет количество запросов в заданный промежуток времени и сбрасывает
     * счетчики на ноль.</li>
     * </ol>
     * Благодаря ключевому слову synchronized, метод реализует подход therad-safe.
     * @param de - сущность {@link DocumentEntity}
     * @param signature - подпись {@link String}
     * @return - сущность {@link DocumentEntity}
     */
    public synchronized DocumentEntity createDocument(DocumentEntity de, String signature) {

        //пока счетчик запросов меньше лимита и пока время между первым запросом и последним меньше указанного
        // в конструкторе, то документ возвращается из метода.
        while (counter <= requestLimit && (timeUnit + startTime) <= System.currentTimeMillis()) {
            de.setDoc_id(signature);
            System.out.println(de.getDoc_id() + " с подписью " + de.getOwner_inn() + " создан");
            return de;
        }

        //если счетчик превысил свой предел и так же превышен лимит времени, то счетчики сбрасываются
        if (counter > requestLimit && timeUnit > (timeUnit + startTime)) {
            counter = 0;
            startTime = 0;
        }
        return null;
    }
}


/**
 * Класс отражает структуру JSON запроса на <a href="https://ismp.crpt.ru/api">ismp.crpt.ru/api</a>
 */
class DocumentEntity implements Serializable {
    private String reg_number;

    private String production_date;

    private Description description;

    private String doc_type;

    private String doc_id;

    private String owner_inn;

    private List<Products> products;

    private String reg_date;

    private String participant_inn;

    private String doc_status;

    private Boolean importRequest;

    private String production_type;

    private String producer_inn;

    public String getReg_number() {
        return this.reg_number;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }

    public String getProduction_date() {
        return this.production_date;
    }

    public void setProduction_date(String production_date) {
        this.production_date = production_date;
    }

    public Description getDescription() {
        return this.description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getDoc_type() {
        return this.doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getDoc_id() {
        return this.doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getOwner_inn() {
        return this.owner_inn;
    }

    public void setOwner_inn(String owner_inn) {
        this.owner_inn = owner_inn;
    }

    public List<Products> getProducts() {
        return this.products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public String getReg_date() {
        return this.reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getParticipant_inn() {
        return this.participant_inn;
    }

    public void setParticipant_inn(String participant_inn) {
        this.participant_inn = participant_inn;
    }

    public String getDoc_status() {
        return this.doc_status;
    }

    public void setDoc_status(String doc_status) {
        this.doc_status = doc_status;
    }

    public Boolean getImportRequest() {
        return this.importRequest;
    }

    public void setImportRequest(Boolean importRequest) {
        this.importRequest = importRequest;
    }

    public String getProduction_type() {
        return this.production_type;
    }

    public void setProduction_type(String production_type) {
        this.production_type = production_type;
    }

    public String getProducer_inn() {
        return this.producer_inn;
    }

    public void setProducer_inn(String producer_inn) {
        this.producer_inn = producer_inn;
    }

    public static class Description implements Serializable {
        private String participantInn;

        public String getParticipantInn() {
            return this.participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Description that = (Description) o;
            return Objects.equals(participantInn, that.participantInn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(participantInn);
        }

        @Override
        public String toString() {
            return "Description{" +
                    "participantInn='" + participantInn + '\'' +
                    '}';
        }
    }

    public static class Products implements Serializable {
        private String uitu_code;

        private String certificate_document_date;

        private String production_date;

        private String certificate_document_number;

        private String tnved_code;

        private String certificate_document;

        private String producer_inn;

        private String owner_inn;

        private String uit_code;

        public String getUitu_code() {
            return this.uitu_code;
        }

        public void setUitu_code(String uitu_code) {
            this.uitu_code = uitu_code;
        }

        public String getCertificate_document_date() {
            return this.certificate_document_date;
        }

        public void setCertificate_document_date(String certificate_document_date) {
            this.certificate_document_date = certificate_document_date;
        }

        public String getProduction_date() {
            return this.production_date;
        }

        public void setProduction_date(String production_date) {
            this.production_date = production_date;
        }

        public String getCertificate_document_number() {
            return this.certificate_document_number;
        }

        public void setCertificate_document_number(String certificate_document_number) {
            this.certificate_document_number = certificate_document_number;
        }

        public String getTnved_code() {
            return this.tnved_code;
        }

        public void setTnved_code(String tnved_code) {
            this.tnved_code = tnved_code;
        }

        public String getCertificate_document() {
            return this.certificate_document;
        }

        public void setCertificate_document(String certificate_document) {
            this.certificate_document = certificate_document;
        }

        public String getProducer_inn() {
            return this.producer_inn;
        }

        public void setProducer_inn(String producer_inn) {
            this.producer_inn = producer_inn;
        }

        public String getOwner_inn() {
            return this.owner_inn;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public String getUit_code() {
            return this.uit_code;
        }

        public void setUit_code(String uit_code) {
            this.uit_code = uit_code;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Products products = (Products) o;
            return Objects.equals(uitu_code, products.uitu_code) && Objects.equals(
                    certificate_document_date,
                    products.certificate_document_date) &&
                    Objects.equals(production_date, products.production_date) &&
                    Objects.equals(certificate_document_number,
                            products.certificate_document_number) &&
                    Objects.equals(tnved_code,
                            products.tnved_code) &&
                    Objects.equals(certificate_document,
                            products.certificate_document) &&
                    Objects.equals(producer_inn,
                            products.producer_inn) &&
                    Objects.equals(owner_inn,
                            products.owner_inn) &&
                    Objects.equals(uit_code,
                            products.uit_code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    uitu_code,
                    certificate_document_date,
                    production_date,
                    certificate_document_number,
                    tnved_code,
                    certificate_document,
                    producer_inn,
                    owner_inn,
                    uit_code);
        }

        @Override
        public String toString() {
            return "Products{" +
                    "uitu_code='" + uitu_code + '\'' +
                    ", certificate_document_date='" + certificate_document_date + '\'' +
                    ", production_date='" + production_date + '\'' +
                    ", certificate_document_number='" + certificate_document_number + '\'' +
                    ", tnved_code='" + tnved_code + '\'' +
                    ", certificate_document='" + certificate_document + '\'' +
                    ", producer_inn='" + producer_inn + '\'' +
                    ", owner_inn='" + owner_inn + '\'' +
                    ", uit_code='" + uit_code + '\'' +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentEntity that = (DocumentEntity) o;
        return Objects.equals(reg_number, that.reg_number) &&
                Objects.equals(production_date, that.production_date) &&
                Objects.equals(description, that.description) &&
                Objects.equals(doc_type, that.doc_type) &&
                Objects.equals(doc_id, that.doc_id) &&
                Objects.equals(owner_inn, that.owner_inn) &&
                Objects.equals(products, that.products) &&
                Objects.equals(reg_date, that.reg_date) &&
                Objects.equals(participant_inn, that.participant_inn) &&
                Objects.equals(doc_status, that.doc_status) &&
                Objects.equals(importRequest, that.importRequest) &&
                Objects.equals(production_type, that.production_type) &&
                Objects.equals(producer_inn, that.producer_inn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                reg_number,
                production_date,
                description,
                doc_type,
                doc_id,
                owner_inn,
                products,
                reg_date,
                participant_inn,
                doc_status,
                importRequest,
                production_type,
                producer_inn);
    }

    @Override
    public String toString() {
        return "DocumentEntity{" +
                "reg_number='" + reg_number + '\'' +
                ", production_date='" + production_date + '\'' +
                ", description=" + description +
                ", doc_type='" + doc_type + '\'' +
                ", doc_id='" + doc_id + '\'' +
                ", owner_inn='" + owner_inn + '\'' +
                ", products=" + products +
                ", reg_date='" + reg_date + '\'' +
                ", participant_inn='" + participant_inn + '\'' +
                ", doc_status='" + doc_status + '\'' +
                ", importRequest=" + importRequest +
                ", production_type='" + production_type + '\'' +
                ", producer_inn='" + producer_inn + '\'' +
                '}';
    }
}
