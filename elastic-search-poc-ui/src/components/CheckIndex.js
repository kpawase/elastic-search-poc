import React, {Component} from 'react';
import checkIndexService from '../services/checkIndexService';

const url = 'http://localhost:8081/transaction/v1/index';
class CheckIndex extends Component{

        constructor(props){
            super(props);
            this.state = {
                indexCreationFlag:false,
                isIndexPresent: false,
                indexName:'No Index Present',
            }

        }

        onCreateIndex = async (e) => {
            this.setState ((state,props) =>{

                return {
                    indexCreationFlag : true,
                }
            })

            e.preventDefault();
             await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(async response => response.json()).then((response) =>
    
                this.setState((state, props) => {
    
                    return {
                        isIndexPresent : true,
                        indexName : response.data.indexName,
                    }
                }
    
                )
            );
                 window.location.reload(true);
                 window.alert("Index Created");
        }


        onDeleteIndex = async (e) => {

            e.preventDefault();
             await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(async response => response.json()).then((response) =>
    
                this.setState((state, props) => {
    
                    return {
                        isIndexPresent : false,
                        indexName : 'No Index Present',
                        indexCreationFlag : false,
                    }
                }
    
                )
            );
                 window.location.reload(true);
                 window.alert("Index Deleted");
        }


        componentDidMount(){
            checkIndexService.checkIndexPresent().then (response => response.json()
            ).then((response) => {
                this.setState((state, props) =>  {
                    return  {isIndexPresent: response.data,
                             indexName: response.message,
                         };
                })
            });
        }
        

        render()
        {
            return(
                <div className="container-fluid">
                {
                    this.state.isIndexPresent ? <div><span>[Index : {this.state.indexName}] &nbsp;&nbsp;&nbsp;</span><button className="btn btn-danger  btn-sm" type="button" onClick={this.onDeleteIndex}> Delete </button> <br/> <br/></div> : 

                    
                    <div><span>[No Index Present]</span> <button className="btn btn-primary  btn-sm" type="button" onClick={this.onCreateIndex} disabled = {this.state.indexCreationFlag ? true : false}> Create Index </button> <br /><br /></div>
                }
                   
                </div>
            );

        }

}


export default CheckIndex