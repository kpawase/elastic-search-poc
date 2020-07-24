import React, { Component } from 'react';
import './App.css';
import CheckIndex from './components/CheckIndex';
import SearchIndex from './components/SearchIndex';


function App(){


  return (
    <div className="App">
    <h1>ElasticSearch-POC</h1>
    <hr className = "Hr" />
      <CheckIndex />

    <SearchIndex />

    </div>
  );

}

export default App;
