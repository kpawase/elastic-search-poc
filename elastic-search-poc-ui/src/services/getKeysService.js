

const url = 'http://localhost:8081/transaction/v1/keys';

class getKeys {

    getIndexKeys(){

        return fetch(url);

    }

}

export default new getKeys(); 