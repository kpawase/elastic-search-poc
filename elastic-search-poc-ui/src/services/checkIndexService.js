const url = 'http://localhost:8081/transaction/v1/index';

class checkIndex {

    checkIndexPresent(){

        return fetch(url);

    }

}

export default new checkIndex(); 