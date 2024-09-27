
import PocketBase from 'pocketbase';

class API {
  public static getProvider(): PocketBase {
    const pb = new PocketBase('https://flashy-pocketbase.bkx.es');
    return pb;
  }
}

export { API };