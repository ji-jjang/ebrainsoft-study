import { useEffect } from "react";
import { fetchHelloData } from "../services/testService.js";

const Test = () => {

  useEffect(
    () => {
      const loadData = async () => {
        const ret = await fetchHelloData(); // fetchHelloData 호출
        console.log(ret); // API 응답 데이터 로깅
      };
      loadData();
    }, []);

  return <div>API Test</div>;
};

export default Test;