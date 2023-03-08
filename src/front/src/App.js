import './App.css';
import axios from 'axios';
import React from 'react'

function App() {
    const logo = 'My First Kafka';

    return (
        <div className="App">
            <div className="black-nav">
                <h4>{logo}</h4>
            </div>
            <div className="contents">
                <div className="left brick">
                    <h1>producer</h1>
                    <input type={"text"} id='proInput' />
                    <button onClick={() => sendEvent()}>전송</button>
                </div>
                <div className="right brick">
                    <h1>consumer</h1>
                    <div className={'box'}>
                        <h4>data</h4>
                        <h4>data</h4>
                        <h4>data</h4>
                        <h4>data</h4>
                        <h4>data</h4>
                    </div>
                </div>
            </div>
        </div>
    );
}

function sendEvent(e) {
    const url = 'http://localhost:8080/kafka/send';
    const element = document.getElementById('proInput');
    const value = String(element.value);

    // 전송 API 콜
    axios.post(url, {
            message: value
        }, {
            withCredentials: true
    }).then((response) => {
        if (response.status === 200) { // 성공
            element.value = '';
        }
    }).catch((response) => {
        console.log(response);
        alert('전송 실패');
    })
}

export default App;
