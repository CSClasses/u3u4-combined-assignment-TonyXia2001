/*
Unit 3&4 Assignment 1 Question 2
Programmer: Tony Xia
Date: 5/21/2019
Modifications: None
Description: The following program reads data in and displays the weather data
*/

var numbers = [String]()
var posiDouble = [String]()
var posiInt = [Int]()
var negatives = [String]()

func main() {
    welcomeUser()
    takeInputs()
    sortInputs()
    outputResults()
}

// print the welcome message
func welcomeUser() {
    print("Welcome to the number sorter")
    print("type in an array of numbers, separated with space")
    print("and we will sort the numbers into three groups: positive whole numbers, positive decimal numbers, and negative numbers")
}

// stores the userinput in an array of integers
func takeInputs() {
    print("Please type in your array in the following line:")
    var input = ""
    var inputArr = [Substring]()

    // take the inputs
    while input.isEmpty {
        guard let inputString = readLine() else {
            print("Invalid input!")
            continue
        }
        input = inputString
        inputArr = input.split(separator: " ")
        // iterate through the array to check for illegal input
        for var i in 0...(inputArr.count - 1) {
            numbers.append(String(inputArr[i]))

            // if any element can't be converted into Double, it's not a number
            guard let a = Double(numbers[i]) else {
                print("Invalid input!")

                // reset to take the input again
                input = ""
                numbers = [String]()
                break
            }
        }
    }
}

// classify the inputs into three arrays
func sortInputs() {
    for var each in numbers {
        if each[each.startIndex] == "-" {
            negatives.append(each)
        } else if let a = Int(each) {
            posiInt.append(a)
        } else {
            posiDouble.append(each)
        }
    }
}

//
func outputResults() {
    print("here are the positive integers:")
    for var each in posiInt {
        print(each, terminator: ", ")
    }
    print("--")
    print("here are the positive doubles:")
    for var each in posiDouble {
        print(each, terminator: ", ")
    }
    print("--")
    print("here are the negative numbers:")
    for var each in negatives {
        print(each, terminator: ", ")
    }
    print("--")
}
main()
