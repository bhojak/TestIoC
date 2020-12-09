package ioc.activation

import java.lang.Exception

class MissingBindingException(details: String) : Exception(details)