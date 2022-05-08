package main

import (
	"fmt"
	"io/ioutil"
	"strings"
)

type StatMethodType int32

const (
	SENTENCE_CONTAINS_CASE StatMethodType = iota
	SENTENCE_CONTAINS_ICASE
	SENTENCE_START_WITH_CASE
	SENTENCE_START_WITH_ICASE
	WORD_CONTAINS_CASE
	WORD_CONTAINS_ICASE
	WORD_START_WITH_CASE
	WORD_START_WITH_ICASE
)

type BookType struct {
	name      string
	file      string
	text      string
	sentences []string
	words     []string
	stats     []StatValuesType
}

type StatValuesType struct {
	book     *BookType
	method   StatMethodType
	counts   []int64
	duration int64
}

var books = []BookType{
	{
		name: "Bible",
		file: "tmp/ACV.txt",
	},
	/*	{
		"Quran",
		"tmp/qime.txt",
		"",
	},*/
}

var needles = []string{"god", "hap", "para"}

func cmpContainsCase(haystack string, needle string) bool {
	return strings.Contains(haystack, needle)
}

func cmpContainsICase(haystack string, needle string) bool {
	return strings.Contains(strings.ToLower(haystack), strings.ToLower(needle))
}

func cmpStartsWithCase(haystack string, needle string) bool {
	return strings.HasPrefix(haystack, needle)
}

func cmpStartsWithICase(haystack string, needle string) bool {
	return strings.HasPrefix(strings.ToLower(haystack), strings.ToLower(needle))
}

func processSlice(haystack *[]string, needles *[]string, cmpFunc func(string, string) bool) []int64  {
	var stat = make([]int64, len(*needles))
	for i := range stat {
		stat[i] = 0
	}
	for _, h := range *haystack {
		for i, n := range *needles {
			if cmpFunc(h, n) {
				stat[i]++
			}
		}
	}
	return stat
}

func processBook(book *BookType) {

	s := processSlice(&book.sentences, &needles, cmpContainsCase)
}

func main() {

	fmt.Print("Start")

	for _, book := range books {

		content, err := ioutil.ReadFile(book.file)
		if err != nil {
			fmt.Println("Error reading book: " + book.file)
		}
		book.text = string(content)
		r := strings.NewReplacer("\n", " ", "\r", "", "\t", "", "  ", " ", "  ", " ")
		book.text = r.Replace(book.text)

		book.words = strings.Fields(book.text)
		for i, _ := range book.words {
			book.words[i] = strings.TrimSpace(book.words[i])
		}

		book.sentences = strings.Split(book.text, ".")
		for i, _ := range book.words {
			book.sentences[i] = strings.TrimSpace(book.sentences[i])
		}

		processBook(&book)
	}
}
