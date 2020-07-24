import React, { Component } from 'react';
import getKeysService from '../services/getKeysService';
import Modal from './Modal'

class SearchIndex extends Component {



    constructor(props) {
        super(props);
        this.state = {
            keys: [],
            searchLimit: [10, 50, 100,200],
            key: 'DEFAULT',
            value: '',
            criteria: '',
            limit: 10,
            searchResponse: {},
            searchedRows: [],
            executionDetails: {},
            showModal: false,
        };
    }

    componentDidMount() {
        getKeysService.getIndexKeys().then(response => response.json()).then
            ((response) =>
                this.setState((state, props) => {
                    return {
                        keys: response.data
                    };
                })
            )

    }

    onChange = (e) => {
        let name = e.target.name;
        let val = e.target.value;
        this.setState({ [name]: val })
    }

    onModalClose = (e) => {
        this.setState({
            showModal: false,
        })

    }


    getAllDocs =async (e) => {
        await this.setState({
            key: 'DEFAULT',
            value: '',
            criteria: '',
            limit: 100,
        })
        document.getElementById("submit-form").reset();
        this.onSubmit(e);
    }

    onSubmit = (e) => {

        e.preventDefault();
        const response = fetch('http://localhost:8081/transaction/v1/indexdata', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                key: this.state.key,
                value: this.state.value,
                criteria: this.state.criteria,
                limit: this.state.limit
            })
        }).then(async response => response.json()).then((response) =>

            this.setState((state, props) => {

                return {
                    searchResponse: response.data,
                    searchedRows: response.data.searchResult,
                    executionDetails: response.data.executionTime,
                }
            }

            )
        );

        if (this.state.searchedRows.length == 0)
            this.setState({ showModal: true });

        console.log(this.state.searchResponse);


    }

    render() {

        return (
            <div className="SearchIndex">
                <div className="container-fluid">
                    <form onSubmit={this.onSubmit} id="submit-form">
                        <table className="table table-responsive table-sm" >
                            <tbody>
                                <tr><td><label>Key:</label></td>
                                    <td>
                                        <select id="key" required defaultValue={'DEFAULT'} name='key' className="form-control form-control-sm" onChange={this.onChange}>
                                            <option value="DEFAULT" value> -- select the key -- </option>
                                            {
                                                Object.keys(this.state.keys).map(key => {
                                                    return <option value={this.state.keys[key]} key={key}>{this.state.keys[key]}</option>
                                                })
                                            }
                                        </select>
                                    </td>
                                </tr>
                                <tr> <td><label>Criteria:</label></td>
                                    <td>
                                        <select id="criteria" defaultValue={'DEFAULT'} className="form-control form-control-sm" name='criteria' onChange={this.onChange}>
                                            <option value="DEFAULT" value> -- select the criteria -- </option>
                                            <option value="must_match">Must Match</option>
                                            <option value="should_match">Should Match</option>
                                            <option value="must_not">Must Not</option>
                                        </select>


                                    </td>
                                </tr>
                                <tr><td><label>Value:</label></td>
                                    <td> <input
                                        type="text"
                                        className="form-control form-control-sm"
                                        placeholder="Value"
                                        name="value"
                                        onChange={this.onChange}
                                        required
                                    /></td>
                                </tr>
                                <tr><td><label>Search Limit:</label></td>
                                    <td>
                                        <select name='limit' className="form-control form-control-sm" onChange={this.onChange}>
                                            {
                                                Object.keys(this.state.searchLimit).map(key => {
                                                    return <option value={this.state.searchLimit[key]} key={key}>{this.state.searchLimit[key]}</option>
                                                })
                                            }
                                        </select>
                                    </td>
                                </tr>

                                <tr><td> <button className="btn btn-primary  btn-sm" type="submit">&nbsp;&nbsp; Search &nbsp;&nbsp;</button> </td>
                                    <td><button className="btn btn-primary  btn-sm" type="button" onClick={this.getAllDocs}> Get All Docs</button></td></tr>
                            </tbody>
                        </table>
                    </form>
                </div>
                <div>
                    {
                        this.state.searchedRows.length > 0 ?


                            <div><br />
                                <label> Execution Time :  {this.state.executionDetails.secondsFrac} Seconds</label>
                                <table className="table table-sm table-dark table-hover">
                                    <thead>
                                        <tr>
                                            <td>Account No</td>
                                            <td>Date</td>
                                            <td>Transaction Details</td>
                                            <td>Cheque No</td>
                                            <td>Value Date</td>
                                            <td>Withdrawal amount</td>
                                            <td>Deposit Amount</td>
                                            <td>Balance Amount</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            this.state.searchedRows.map(
                                                searchedRow =>
                                                    <tr>
                                                        <td>{searchedRow.accountNo}</td>
                                                        <td>{searchedRow.date}</td>
                                                        <td>{searchedRow.transactionDetails}</td>
                                                        <td>{searchedRow.chequeNo}</td>
                                                        <td>{searchedRow.valueDate}</td>
                                                        <td>{searchedRow.withdrawalAmount}</td>
                                                        <td>{searchedRow.depositAmount}</td>
                                                        <td>{searchedRow.balance}</td>

                                                    </tr>
                                            )
                                        }
                                    </tbody>
                                </table>
                            </div>
                            :

                            <div>
                                <Modal
                                    className="modal"
                                    show={this.state.showModal}
                                    close={this.onModalClose}>
                                    No records found for the given criteria.
                                    
                </Modal>
                            </div>

                    }
                </div>
            </div>

        );


    }


}

export default SearchIndex;