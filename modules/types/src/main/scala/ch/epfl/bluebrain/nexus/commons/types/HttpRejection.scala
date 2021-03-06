package ch.epfl.bluebrain.nexus.commons.types

sealed trait HttpRejection extends Rejection

object HttpRejection {

  /**
    * Signals the inability to find a resource associated to a particular HTTP verb
    *
    * @param supported the collections of supported HTTP verbs for a particular resource
    */
  @SuppressWarnings(Array("IncorrectlyNamedExceptions"))
  final case class MethodNotSupported(supported: Seq[String]) extends HttpRejection

  /**
    * Signals the inability to convert the Payload into JSON. It can be due to invalid JSON
    * syntax or due to constraints in the implemented JSON Decoder
    *
    * @param details optional explanation about what went wrong while parsing the Json payload
    */
  @SuppressWarnings(Array("IncorrectlyNamedExceptions"))
  final case class WrongOrInvalidJson(details: Option[String]) extends Err("Invalid json") with HttpRejection

  /**
    * Signals that query parameters are missing
    *
    * @param missingParams the list of the missing parameters
    *
    */
  @SuppressWarnings(Array("IncorrectlyNamedExceptions"))
  final case class MissingParameters(missingParams: Seq[String])
      extends Err(s"""Missing query parameters: ${missingParams.mkString(", ")}""")
      with HttpRejection

  /**
    * Signals the inability to convert a path segment into a [[ch.epfl.bluebrain.nexus.commons.types.Version]]
    *
    */
  @SuppressWarnings(Array("IncorrectlyNamedExceptions"))
  final case class IllegalVersionFormat(override val message: String) extends Err(message) with HttpRejection

  /**
    * Signals that the caller does not have access to this resource
    *
    */
  @SuppressWarnings(Array("IncorrectlyNamedExceptions"))
  final case object UnauthorizedAccess extends Err("Unauthorized access to the current resource") with HttpRejection
}
