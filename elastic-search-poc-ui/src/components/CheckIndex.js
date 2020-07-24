import React, {Component} from 'react';
import checkIndexService from '../services/checkIndexService';
class CheckIndex extends Component{

        constructor(props){
            super(props);
            this.state = {
                 
                isIndexPresent: false,
                indexName:'No Index Present',
            }

        }

        onCreateIndex = async (e) => {
            e.preventDefault();
             await fetch('http://localhost:8081/transaction/v1/index', {
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
                    this.state.isIndexPresent ? <h4>[Index : {this.state.indexName}]</h4> :  <div><span>[No Index Present]</span> <button className="btn btn-primary  btn-sm" type="button" onClick={this.onCreateIndex}> Create Index </button> <br /><br /></div>
                }
                   
                </div>
            );

        }

}


export default CheckIndex