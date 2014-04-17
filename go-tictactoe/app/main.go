// This package performs initialization during startup.
// 
// It is separated from tictactoe package so that the latter can be used
// in another app, e.g. http://github.com/crhym3/go-endpoints.appspot.com.

package app

import (
	"github.com/PinkFairyArmadillos/go-endpoints/endpoints"
	"github.com/PinkFairyArmadillos/go-tictactoe/tictactoe"
)

func init() {
	if _, err := tictactoe.RegisterService(); err != nil {
		panic(err.Error())
	}
	endpoints.HandleHttp()
}
