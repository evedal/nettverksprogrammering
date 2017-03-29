#include <iostream>
#include <vector>
#include <math.h>
#include <thread>
#include <mutex>
using namespace std;


bool isPrime(int n){
  if(n <= 1) return false;
  else if (n <= 3) return true;
  else if (n % 2 == 0 || n % 3 == 0) return false;
  int i = 5;
  while(i*i <= n) {
    if (n % i == 0 || n % (i+2) == 0) return false;
    i = i + 6;
  }
  return true;
}
vector<int> primes;
mutex primes_mutex;

vector<int> findPrimes(int start, int end){
  for (int i = start; i <= end; ++i){
    if(isPrime(i)) {
      lock_guard<mutex> lock(primes_mutex);
      primes.push_back(i);

    }
    
  }
  return primes;
}

int main() {
  const int THREAD_COUNT = 11;
    const int START_NUMB = 1;
  const int END_NUMB = 1000;
  const int STEP = floor((END_NUMB - START_NUMB)/THREAD_COUNT);
  vector<thread> threads;
  int threadEndNumb;

  for(int i = START_NUMB; i <= END_NUMB; i += STEP){
    threadEndNumb = i + STEP;
    if(threadEndNumb > END_NUMB) threadEndNumb = END_NUMB;
    threads.push_back(thread([i, threadEndNumb] (){
      findPrimes(i, threadEndNumb);
    }));
  }

  for(unsigned i = 0; i < threads.size(); ++i){
    threads[i].join();
  }


  for(unsigned i = 0; i < primes.size(); ++i){

    cout << primes[i] << ' ';
  }
  return 0;
}
