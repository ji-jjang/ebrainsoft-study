import { useEffect } from "react";
import { helloApi } from "../services/testService.js";

const Test = () => {
  useEffect(() => {
    const loadData = async () => {
      const ret = await helloApi();
      console.log(ret);
    };
    loadData();
  }, []);

  return <div>API Test</div>;
};

export default Test;
